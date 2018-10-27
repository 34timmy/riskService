package ru.mifi.service.risk.domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mifi.service.risk.domain.enums.AggregateTypeEnum;
import ru.mifi.service.risk.exception.DataLeakException;
import ru.mifi.service.risk.exception.WrongFormulaValueException;
import ru.mifi.service.risk.utils.validation.ValidationUtil;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.*;
import java.util.stream.Stream;

/**
 * Класс обработки данных. Выполняет логику по расчету агрегатов и строк баланса.
 *
 * @author DenRUS on 20.10.2017.
 */
public class DataReplacer {
    private static final Logger LOG = LoggerFactory.getLogger(ValidationUtil.class);

    /**
     * Коллекция иннов в разработке
     */
    private Set<String> innsInDevelop;
    /**
     * Текущий год разработки
     */
    private int yearInDevelop;

    /**
     * Коллекция всех инн в данных
     */
    private Set<String> allInns;

    /**
     * Кэш
     */
    private Map<String, String> replacerCache = new HashMap<>();

    /**
     * Удалить/создать кэш
     */
    public void initializeCache() {
        replacerCache = new HashMap<>();
    }

    /**
     * Данные
     */
    private Map<DataKey, Map<String, Double>> data;

    /**
     * Конструктор
     *
     * @param data          данные
     * @param innsInDevelop инн в анализа
     * @param yearInDevelop год анализа
     * @param allInns       все инн
     */
    public DataReplacer(Map<DataKey, Map<String, Double>> data, Set<String> innsInDevelop,
                        int yearInDevelop, Set<String> allInns) {
        this.innsInDevelop = innsInDevelop;
        this.yearInDevelop = yearInDevelop;
        this.allInns = allInns;
        this.data = data;
    }

    /**
     * ЗАмена идентификаторов строк баланса в формула на их занчения
     *
     * @param formula          формула
     * @param curYearData      данные текущего года
     * @param prevYearData     данные предыдущего года
     * @param prevPrevYearData данные предпредыдущего года
     * @return формула со значениями вместо идентификаторов
     * @throws DataLeakException Ошибка нехватки данных в разработке.
     */
    public String replaceBalanceRowIdWithValue(String formula,
                                               Map<String, Double> curYearData,
                                               Map<String, Double> prevYearData,
                                               Map<String, Double> prevPrevYearData) throws DataLeakException {
        Map<String, Double> notNullYearData = Stream.of(curYearData,
                prevYearData, prevPrevYearData)
                .filter(Objects::nonNull)
                .findAny().orElseThrow(() -> new DataLeakException("Нет данных ни по одному году"));

        for (String key : notNullYearData.keySet()) {

            if (curYearData != null && curYearData.get(key) != null) {
                String curVal = curYearData.get(key).toString();

                formula = formula.replaceAll(key + "_C", curVal);
            }
            if (prevYearData != null && prevYearData.get(key) != null) {
                String prevVal = prevYearData.get(key).toString();
                formula = formula.replaceAll(key + "_P", prevVal);
            }
            if (prevPrevYearData != null && prevPrevYearData.get(key) != null) {
                String prevPrevVal = prevPrevYearData.get(key).toString();
                formula = formula.replaceAll(key + "_T", prevPrevVal);
            }
        }
        if (formula.contains("SB")) {
            throw new DataLeakException("Нет данных для формулы по какому-либо году. Формула: " + formula);
        }
        return formula;
    }

    /**
     * Оборачиваем вызов либы в метод
     *
     * @param formula формула для расчета
     * @return рассчитанное значение
     */
    public double calculateExpression(String formula) {
        return new Expression(
                formula
                        .toLowerCase()
                        .replaceAll("\\{", "\\(")
                        .replaceAll("\\}", "\\)")).calculate();
    }

    /**
     * Рассчитывает аггрегаты в формулах
     *
     * @param formula формула
     * @param curInn  текущий инн
     * @return значение формулы после расчета
     * @throws WrongFormulaValueException при ошибке расчета формулы
     * @throws DataLeakException          при нехватке данных
     */
    public double calculateAggregates(String formula, String curInn) throws WrongFormulaValueException, DataLeakException {
        String fomulaWithoutAggregates = replaceAggregatesWithValue(formula);
        return calculateExpression(replaceBalanceRowIdWithValue(fomulaWithoutAggregates,
                data.get(new DataKey(yearInDevelop, curInn)),
                data.get(new DataKey(yearInDevelop, curInn)),
                data.get(new DataKey(yearInDevelop, curInn))));
    }

    /**
     * Заменяет агрегаты на их значения, но результат - строка
     *
     * @param formula формула
     * @return формула без агрегатов
     * @throws WrongFormulaValueException если неправильное количество скобок/иные ошибки в формуле
     */
    private String replaceAggregatesWithValue(String formula) throws WrongFormulaValueException, DataLeakException {
        if (formula.contains(")END")) {
            for (AggregateTypeEnum action : AggregateTypeEnum.values()) {
                formula = calculateAggregateWithPostfix("_ALL(", formula, action, allInns);
                formula = calculateAggregateWithPostfix("(", formula, action, innsInDevelop);
            }
        }
        return formula;
    }

    /**
     * Метод расчета всех агрегатов action в строке formula с постфиксом postFix
     *
     * @param postFix постфикс агрегата ("" или "_ALL")
     * @param formula формула для расчета
     * @param action  агрегат
     * @param inns    инн для расчета
     * @return строку с выполнеными заменами агрегатов
     * @throws WrongFormulaValueException При неверной формуле
     * @throws DataLeakException          При нехватке данных
     */
    private String calculateAggregateWithPostfix(String postFix, String formula,
                                                 AggregateTypeEnum action,
                                                 Set<String> inns) throws WrongFormulaValueException, DataLeakException {

        while (formula.contains(action.getName() + postFix)) {
            int actionPos = formula.indexOf(action.getName() + postFix);
            int endActionPos = getEndIndex(formula, actionPos);
            String fullAggregateArgument = formula.substring(
                    actionPos + action.getName().length() + postFix.length(),
                    endActionPos);
//            final int aggregateLength = fullAggregateArgument.length();
            String fullAggregate = action.getName() + postFix + fullAggregateArgument + ")END";
            final String valueForReplace;
            if (replacerCache.keySet().contains(fullAggregate)) {
                valueForReplace = replacerCache.get(fullAggregate);
            } else {

                List<Double> results = new LinkedList<>();
                String aggregateArgument = replaceAggregatesWithValue(fullAggregateArgument);


                for (String inn : inns) {
                    try {
                        Map<String, Double> curYearData = data.get(new DataKey(yearInDevelop, inn));
                        Map<String, Double> prevYearData = data.get(new DataKey(yearInDevelop - 1, inn));
                        Map<String, Double> prevPrevYearData = data.get(new DataKey(yearInDevelop - 2, inn));
                        results.add(calculateExpression(ValidationUtil.inputCorrection(
                                replaceBalanceRowIdWithValue(aggregateArgument,
                                        curYearData, prevYearData, prevPrevYearData))));
                    } catch (Exception ex) {
                        String infoMsg = "По аггрегату : " + formula + " для инн "
                                + inn + " нет данных для расчета по " + yearInDevelop + " году.";
                        LOG.info(infoMsg);
                    }
                }
                results.removeIf(val -> val.isNaN());
                valueForReplace = String.valueOf(action.calculate(results));
                replacerCache.put(fullAggregate, valueForReplace);
            }
//            String str = (action.getName() + postFix + ".{" + aggregateLength + "}" + ")END")
//                    .replaceAll("\\(", "\\\\(")
//                    .replaceAll("\\)", "\\\\)");
            formula = ValidationUtil.inputCorrection(formula.replaceAll(fullAggregate
                            .replaceAll("\\(", "\\\\(")
                            .replaceAll("\\)", "\\\\)")
                            .replaceAll("\\+", "\\\\+")
                            .replaceAll("\\*", "\\\\*")
                            .replaceAll("\\.", "\\\\.")
                            .replaceAll("\\^", "\\\\^"),
                    valueForReplace));
        }
        return formula;
    }

    /**
     * Получаем номер нужной скобки после агрегата
     *
     * @param formula   формула
     * @param startFrom начальная позиция
     * @return позиция скобки
     * @throws WrongFormulaValueException если количество скобок некорректное
     */
    private int getEndIndex(String formula, int startFrom) throws WrongFormulaValueException {
        char[] chars = formula.substring(startFrom).toCharArray();
        int bracketNum = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ')') {
                if (bracketNum == 1) {
                    return i + startFrom;
                } else {
                    bracketNum--;
                }
            }
            if (chars[i] == '(') {
                bracketNum++;
            }
        }
        throw new WrongFormulaValueException("Неверное количество скобок в формуле");
    }
}
