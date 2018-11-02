package ru.mifi.service.risk.utils.old;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mifi.service.risk.database.DatabaseCalculationAccessor;
import ru.mifi.service.risk.domain.*;
import ru.mifi.service.risk.exception.DataLeakException;
import ru.mifi.service.risk.exception.FormulaCalculationException;
import ru.mifi.utils.CollectionUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис работы с данными.
 *
 * @author DenRUS
 */
@Getter
@RequiredArgsConstructor
public class CalculationService {
    /**
     * Массив неверных формул при вычислении
     */
    private Set<FormulaResult> wrongFormulasFromLastExecution = new HashSet<>();

    private static final Logger LOG = LoggerFactory.getLogger(CalculationService.class);


    private final Set<String> innsInDevelop;
    private final Set<String> allInns;
    private final Set<Formula> formulas;
    private final Map<DataKey, Map<String, Double>> dataKeyMap;
    private final DatabaseCalculationAccessor accessor;
    private final String modelId;


//    public Map<String, Double> calculateOnlyExpression(String input, Map<Integer, JSONArray> mapYearInn) throws JSONException, DataLeakException {
////        inn, result
//        Map<String, Double> resultMap = new HashMap<>();
//        for (Integer year : mapYearInn.keySet()) {
//            for (int arNum = 0; arNum < mapYearInn.get(year).length(); arNum++) {
//                String inn = null;
//                try {
//                    inn = mapYearInn.get(year).getString(arNum);
//
//                    Map<String, Double> curYearData = excelLoader.getData().get(new DataKey(year, inn));
//                    Map<String, Double> prevYearData = excelLoader.getData().get(new DataKey(year - 1, inn));
//                    Map<String, Double> prevPrevYearData = excelLoader.getData().get(new DataKey(year - 2, inn));
//
//                    DataReplacer replacer = new DataReplacer(this.getData(), this.getInnsInDevelop(), year, this.getAllInns());
//
//                    String formulaAfterReplace;
//
//
//                    formulaAfterReplace = replacer.replaceBalanceRowIdWithValue(input.toUpperCase(),
//                            curYearData, prevYearData, prevPrevYearData);
//
//                    if (formulaAfterReplace.contains("NaN")) {
//                        throw new DataLeakException(String.format("Формула \t{ %s } не используется в расчетах из-за невалидных данных", input));
//                    } else {
//                        resultMap.put(inn, replacer.calculateExpression(formulaAfterReplace));
//
//                    }
//
//                } catch (JSONException | DataLeakException ex) {
//                    LOG.warn(String.format("Параметры: Инн = %s, Год = %s\nВ формуле по id:%s\nОшибка: \n\t{\n\t\t%s.\n\t}",
//                            inn, year, null, ex.getMessage()));
//                }
//            }
//        }
//
//        return resultMap;
//
//    }

    /**
     * Расчет набора формул на конкретных параметрах: год и инн
     *
     * @param year год для расчета
     * @param inn  компания для расчета
     * @return Мапа (ID формулы, результат)
     */
    public Map<String, FormulaResult> calculateFormulas(
            int year,
            String inn,
            Set<Formula> formulas,
            Map<DataKey, Map<String, Double>> dataKeyMap
    ) {
        Map<String, FormulaResult> resultMap = new HashMap<>();
        StringJoiner emptyYear = new StringJoiner(", ");
        try {
            Map<String, Double> curYearData = dataKeyMap.get(new DataKey(year, inn));
            Map<String, Double> prevYearData = dataKeyMap.get(new DataKey(year - 1, inn));
            Map<String, Double> prevPrevYearData = dataKeyMap.get(new DataKey(year - 2, inn));
            if (curYearData == null) {
                emptyYear.add(String.valueOf(year));
            }
            if (prevYearData == null) {
                emptyYear.add(String.valueOf(year - 1));
            }
            if (prevPrevYearData == null) {
                emptyYear.add(String.valueOf(year - 2));
            }
            if (emptyYear.length() != 0) {
                LOG.error(String.format(" Инн = %s. Отсутствует данные за %s год.", inn, emptyYear));
            }
            DataReplacer replacer = new DataReplacer(dataKeyMap, this.getInnsInDevelop(), year, this.getAllInns());
            Set<FormulaResult> wrongFormulas = new HashSet<>();

            replacer.initializeCache();
            for (Formula formula : formulas) {
                String formulaAfterReplace;
                try {

                    formulaAfterReplace = replacer.replaceBalanceRowIdWithValue(formula.getCalculationFormula(),
                            curYearData, prevYearData, prevPrevYearData);

                    if (formulaAfterReplace.contains("NaN")) {
                        throw new DataLeakException(String.format("Формула \t{ %s } не используется в расчетах из-за невалидных данных", formula));
                    } else {
                        resultMap.put(formula.getId(), formula.calculate(formulaAfterReplace, inn, year, replacer));

                    }

                } catch (FormulaCalculationException | DataLeakException ex) {
                    wrongFormulas.add(new FormulaResult(inn, formula.getId(), year));
                    LOG.warn(String.format("Параметры: Инн = %s, Год = %s\nВ формуле по id:%s\nОшибка: \n\t{\n\t\t%s.\n\t}",
                            inn, year, formula.getId(), ex.getMessage()));
                }
            }

            wrongFormulasFromLastExecution.addAll(wrongFormulas);
        } catch (NullPointerException ex) {
            LOG.error(ex.getMessage());
            throw new RuntimeException("Ошибка при расчете формул");
        }
        return resultMap;
    }

    /**
     * Производит расчет формул по заданным условиям с их сборкой по иерархии
     *
     * @param year год анализа
     * @return результат иерархии
     */
    public HierarchyResult calculateFormulasHierarchy(
            int year
    ) {
        wrongFormulasFromLastExecution = new HashSet<>();
//        <inn, simple formulasRes>
        Map<String, Map<String, FormulaResult>> calculationData = new HashMap<>();
//        <id,FormulasRes>
        Map<String, FormulaResult> calculateFormulasResult;
        List<FormulaResult> simpleData = new ArrayList<>();
        for (String inn : this.innsInDevelop) {
            calculateFormulasResult = calculateFormulas(year, inn, formulas, dataKeyMap);
            simpleData.addAll(calculateFormulasResult.values());
            if (calculateFormulasResult.size() != 0) {
                calculationData.put(inn, calculateFormulasResult);
            }
        }

        Map<String, Map<String, FormulaResult>> normalizedData = normalizeFormulasValue(calculationData);

        //<inn, value>
        Map<String, Collection<FormulaResult>> resultMap = new HashMap<>();
        for (String inn : this.innsInDevelop) {
            if (normalizedData.get(inn) != null) {
                resultMap.put(inn, calculateHierarchyByModelCalc(
                        normalizedData.get(inn), inn, year));
            }
        }
        return new HierarchyResult(calculationData, resultMap, simpleData);
    }

    /**
     * Нормализуем значения функций в датасете по всем инн (суммарно по ИНН должно быть = 1)
     *
     * @param calculationData результаты расчета
     * @return нормализованные результаты расчета
     */
    private Map<String, Map<String, FormulaResult>> normalizeFormulasValue(
            Map<String, Map<String, FormulaResult>> calculationData) {
        if (calculationData.keySet().size() <= 1) {
            return calculationData;
        }
        Map<String, Map<String, FormulaResult>> result = new HashMap<>();
        formulas.forEach(formula -> {
            final double[] totalValue = {0};
//            error here null
            calculationData.forEach((key, elem) -> {
                if (elem.get(formula.getId()) != null) {
                    totalValue[0] += elem.get(formula.getId()).getResult();
                }
            });
            calculationData.forEach((key, elem) -> {
                //Передать через computeIfAbsent
                result.putIfAbsent(key, new HashMap<>());
                FormulaResult curFormulaResultObj = elem.get(formula.getId());
                if (curFormulaResultObj != null) {
                    result.get(key).put(formula.getId(), new FormulaResult(key, curFormulaResultObj.getId(),
                            curFormulaResultObj.getInputeValue(),
                            curFormulaResultObj.getResult() / totalValue[0], curFormulaResultObj.getYear(),
                            curFormulaResultObj));
                }
            });
        });
        return result;
    }

    @SneakyThrows
    private Collection<FormulaResult> calculateHierarchyByModelCalc(
            Map<String, FormulaResult> formulasData,
            String inn,
            int year
    ) {
        Map<HierarchyNode, String> idToParent = accessor.getModelCalcHierarchy(modelId);
        Map<String, List<HierarchyNode>> parents = idToParent.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getValue,
                                entry -> Arrays.asList(entry.getKey()),
                                CollectionUtils::mergeLists));
        for (String parentId : parents.keySet()) {
            if (formulasData.containsKey(parentId)) {
                continue;
            }
            Double val = calculateNode(parents, parentId, formulasData);
            formulasData.put(parentId, new FormulaResult(inn, parentId, val, val, year));

        }
        return formulasData.values();

    }

    private Double calculateNode(Map<String, List<HierarchyNode>> children, String nodeId, Map<String, FormulaResult> formulasData) {
        if (formulasData.containsKey(nodeId)) {
            return formulasData.get(nodeId).getResult();
        }
        return children.get(nodeId).stream()
                .mapToDouble(fr -> calculateNode(children, fr.getId(), formulasData) * fr.getWeight())
                .average()
                .getAsDouble();
    }

    /**
     * Вычисляет итоговый риск по конкретной компании (конкретным данынм)
     *
     * @param formulasData результаты расчета по компании
     * @param inn          инн компании
     * @param year         год компании
     * @return коллекция результатов формул по иерархии для компании
     */
    @Deprecated
    private Collection<FormulaResult> calculateHierarchyById(Map<String, FormulaResult> formulasData,
                                                             String inn, int year) {
        List<List<String>> keySet = formulasData.keySet()
                .stream()
                .filter(Objects::nonNull)
                .map(key -> new LinkedList<>(Arrays.asList(key.split("[.]"))))
                .sorted(Comparator.comparingInt(o -> -o.size()))
                .collect(Collectors.toCollection(LinkedList::new));
        while (keySet.size() > 1) {

            List<String> activeElem = keySet.get(0);
            int amount = 0;
            double total = 0;
//            for (List<String> elem : keySet) {
            for (int i = 0; i < keySet.size(); ) {
                if (keySet.get(i).size() == activeElem.size()) {
                    boolean isHierarchical = true;
                    for (int j = 0; j < keySet.get(i).size() - 1; j++) {
                        if (!keySet.get(i).get(j).equals(activeElem.get(j))) {
                            isHierarchical = false;
                            i++;
                            break;
                        }
                    }
                    if (isHierarchical) {
                        amount++;
                        total += formulasData
                                .get(String.join(".", keySet.get(i))
                                ).getResult();
                        keySet.remove(keySet.get(i));
                    }
                } else {
                    i++;
                }
            }
            activeElem.remove(activeElem.size() - 1);
            String formulaId = String.join(".", activeElem);
            formulasData.put(formulaId, new FormulaResult(inn, formulaId, total / amount,
                    total / amount, year));
            keySet.add(activeElem);

            keySet = keySet.stream()
                    .sorted(Comparator.comparingInt(o -> -o.size()))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
//        TODO: keySet пустой
        return formulasData.values();
    }

    /**
     * Получить коллекцию неверных формул с последнего выполнения
     *
     * @return коллекция неверных формул с последнего выполнения
     */
    public Set<FormulaResult> getWrongFormulasFromLastExecution() {
        return wrongFormulasFromLastExecution;
    }
}
