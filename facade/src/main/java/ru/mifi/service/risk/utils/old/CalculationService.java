package ru.mifi.service.risk.utils.old;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mifi.service.risk.database.DatabaseCalculationAccessorImpl;
import ru.mifi.service.risk.domain.DataKey;
import ru.mifi.service.risk.domain.DataReplacer;
import ru.mifi.service.risk.domain.Formula;
import ru.mifi.service.risk.domain.FormulaResult;
import ru.mifi.service.risk.domain.HierarchyNode;
import ru.mifi.service.risk.domain.HierarchyResult;
import ru.mifi.service.risk.domain.enums.FormulaTypeEnum;
import ru.mifi.service.risk.exception.DataLeakException;
import ru.mifi.service.risk.exception.FormulaCalculationException;
import ru.mifi.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Сервис работы с данными.
 *
 * @author DenRUS
 */
@Getter
@RequiredArgsConstructor
public class CalculationService {
    private static final Logger LOG = LoggerFactory.getLogger(CalculationService.class);
    private final Set<String> innsInDevelop;
    private final Set<String> allInns;
    private final Set<Formula> formulas;
    private final Map<DataKey, Map<String, Double>> dataKeyMap;
    private final DatabaseCalculationAccessorImpl accessor;
    private final String modelId;
    /**
     * Массив неверных формул при вычислении
     */
    private Set<FormulaResult> wrongFormulasFromLastExecution = new HashSet<>();


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
            DataReplacer replacer = new DataReplacer(
                    dataKeyMap,
                    this.getInnsInDevelop(),
                    year,
                    this.getAllInns(),
                    accessor
            );
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
                    wrongFormulas.add(new FormulaResult(inn, formula.getId(), year, formula.getNodeName(), ex.getMessage()));
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

        return new HierarchyResult(calculationData, calculateHierarchyByModelCalc(normalizedData, year), simpleData);
    }

    /**
     * Нормализуем значения функций в датасете по всем инн (суммарно по ИНН должно быть = 1)
     *
     * @param calculationData результаты расчета
     * @return нормализованные результаты расчета
     */
    private Map<String, Map<String, FormulaResult>> normalizeFormulasValue(
            Map<String, Map<String, FormulaResult>> calculationData
    ) {
        if (calculationData.keySet().size() <= 1) {
            return calculationData;
        }
        Map<String, Map<String, FormulaResult>> result = new HashMap<>();
        for (Formula formula : formulas) {
            double totalValue = 0;
            double maxVal = Integer.MIN_VALUE;
            double minVal = Integer.MAX_VALUE;
//            error here null
            for (Map<String, FormulaResult> elem : calculationData.values()) {
                FormulaResult curFormulaResult;
                if ((curFormulaResult = elem.get(formula.getId())) != null) {
                    double curVal = curFormulaResult.getResult();
                    totalValue += curVal;
                    if (curVal > maxVal) {
                        maxVal = curVal;
                    }
                    if (curVal < minVal) {
                        minVal = curVal;
                    }
                }
            }
            for (String key : calculationData.keySet()) {
                //Передать через computeIfAbsent
                result.putIfAbsent(key, new HashMap<>());
                FormulaResult curFormulaResultObj = calculationData.get(key).get(formula.getId());
                if (curFormulaResultObj != null) {
                    double normalizedValue = totalValue == 0
                            ? 0
                            : curFormulaResultObj.getResult() / totalValue;
                    double avgVal = totalValue / calculationData.keySet().size();
                    result.get(key).put(
                            formula.getId(),
                            new FormulaResult(
                                    key,
                                    curFormulaResultObj.getId(),
                                    curFormulaResultObj.getInputeValue(),
                                    curFormulaResultObj.getResult(),
                                    normalizedValue,
                                    curFormulaResultObj.getYear(),
                                    minVal == maxVal
                                            ? minVal * 100
                                            : ((curFormulaResultObj.getResult() - minVal) / (maxVal - minVal)) * 100,
                                    FormulaTypeEnum.S.calculate(
                                            normalizedValue,
                                            0,
                                            avgVal,
                                            0, 0,
                                            avgVal * formula.getInterpretationK()),
                                    formula.getInterpretationK(),
                                    curFormulaResultObj
                            )
                    );
                }
            }
        }
        return result;
    }

    @SneakyThrows
    private Map<String, Collection<FormulaResult>> calculateHierarchyByModelCalc(
            Map<String, Map<String, FormulaResult>> fullFormulasData,
            int year
    ) {
        Set<String> calculatedNodes = new HashSet<>();
        Map<HierarchyNode, HierarchyNode> idToParent = accessor.getModelCalcHierarchy(modelId);
        Map<HierarchyNode, List<HierarchyNode>> parents = idToParent.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getValue,
                                entry -> Arrays.asList(entry.getKey()),
                                CollectionUtils::mergeLists));
        for (HierarchyNode parentNode : parents.keySet()) {
            String parentId = parentNode.getId();
            if (calculatedNodes.contains(parentId)) {
                continue;
            }
            List<FormulaResult> nodeResults = new ArrayList<>();
            double totalVal = 0;
            double maxVal = Integer.MIN_VALUE;
            double minVal = Integer.MAX_VALUE;
            for (String inn : fullFormulasData.keySet()) {
                Map<String, FormulaResult> oneCompanyData = fullFormulasData.get(inn);
                Double val = calculateNode(
                        parents.entrySet().stream()
                                .collect(Collectors.toMap(
                                        entry -> entry.getKey().getId(),
                                        Map.Entry::getValue
                                )),
                        parentId,
                        oneCompanyData
                );
                FormulaResult nodeResult = new FormulaResult(
                        inn,
                        parentId,
                        val,
                        val,
                        year,
                        parentNode.getNodeName(),
                        parentNode.getComments()
                );
                oneCompanyData.put(parentId, nodeResult);
                nodeResults.add(nodeResult);

                totalVal += val;
                if (val > maxVal) {
                    maxVal = val;
                }
                if (val < minVal) {
                    minVal = val;
                }
            }
            //Поскольку мы сохранили ссылки на объекты, пройдемся по ним ещё раз и проставим нормированное и
            //смасштабированное значение
            for (FormulaResult nodeResult : nodeResults) {
                nodeResult.setNormalizedResult(totalVal == 0
                        ? 0
                        : nodeResult.getResult() / totalVal);
                nodeResult.setLineadResult(minVal == maxVal
                        ? minVal * 100
                        : ((nodeResult.getResult() - minVal) / (maxVal - minVal)) * 100);
                double avgVal = totalVal / nodeResults.size();
                nodeResult.setInterpretationK(parentNode.getInterpretationK());
                nodeResult.setInterpretationValue(
                        FormulaTypeEnum.S.calculate(
                                nodeResult.getNormalizedResult(),
                                0,
                                avgVal,
                                0, 0,
                                avgVal * nodeResult.getInterpretationK()
                        )
                );
                nodeResult.selfCommentInit();
            }
            calculatedNodes.add(parentId);
        }
        return fullFormulasData.keySet().stream()
                .collect(Collectors.toMap(key -> key, key -> fullFormulasData.get(key).values()));

    }

    private Double calculateNode(Map<String, List<HierarchyNode>> children, String nodeId, Map<String, FormulaResult> formulasData) {
        if (formulasData.containsKey(nodeId)) {
            return formulasData.get(nodeId).getNormalizedResult();
        }
        return children.get(nodeId).stream()
                .mapToDouble(fr -> calculateNode(children, fr.getId(), formulasData) * fr.getWeight())
                .average()
                .getAsDouble();
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
