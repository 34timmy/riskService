package ru.mifi.service.risk.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mifi.service.risk.domain.enums.FormulaTypeEnum;
import ru.mifi.service.risk.exception.FormulaCalculationException;
import ru.mifi.service.risk.exception.WrongFormulaValueException;
import ru.mifi.service.risk.utils.validation.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Класс-представление формулы
 *
 * @author DenRUS on 07.10.2017.
 */
@Data
@NoArgsConstructor
public class Formula {

    /**
     * Названия полей
     */
    public final static String _XB_NAME = "_XB";
    public final static String A_NAME = "A";
    public final static String B_NAME = "B";
    public final static String C_NAME = "C";
    public final static String D_NAME = "D";
    public final static String CALCULATION_INPUT_NAME = "calculationFormula";


    private  String id;
    private  String descr;
    private  FormulaTypeEnum formulaType;
    private  String calculationFormula;
    private String a;
    private String b;
    private String c;
    private String d;
    private String _XB;
    private  double weight;
    private Set<String> comments;
    private double result;
    private double inputValue;
    private String rule_id;

    // this is for constructor
    public Formula(String id, String descr, String formulaType, String calculationFormula, String a, String b,
                   String c, String d, String _XB, String rule_id) {
        this.id = id;
        this.descr = descr;
        this.formulaType = FormulaTypeEnum.valueOf(formulaType);
        this.calculationFormula = calculationFormula;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this._XB = _XB;
        this.weight = 1;
        this.rule_id = rule_id;
    }

    /**
     * Конструктор на основании данных
     *
     * @param id                 идентификатор формулы
     * @param descr              описание формулы
     * @param formulaType        тип формулы
     * @param calculationFormula вычисление входного значения
     * @param a                  первый параметр
     * @param b                  второй параметр
     * @param c                  третий параметр
     * @param d                  четвертый параметр
     * @param _XB                коэффициент к второму параметру
     * @param weight             вес значения
     * @throws WrongFormulaValueException - Если неверное количество скобок
     */
    public Formula(String id, String descr, String calculationFormula, String formulaType, String a, String b, String c,
                   String d, String _XB, double weight) throws WrongFormulaValueException {
        this.id = id;
        this.descr = descr;
        this.formulaType = FormulaTypeEnum.valueOf(formulaType);
        Map<String, String> parameters =
                this.formulaType.getParameters(calculationFormula.replaceAll("[ \t\n]", ""),
                        a, b, c, d, _XB);
        this.calculationFormula = parameters.get(CALCULATION_INPUT_NAME);
        ValidationUtil.formulaBracketsCheck(calculationFormula);
        this.a = parameters.get(A_NAME);
        this.b = parameters.get(B_NAME);
        this.c = parameters.get(C_NAME);
        this.d = parameters.get(D_NAME);
        this._XB = parameters.get(_XB_NAME);
        this.weight = weight;

    }

    public Formula(ResultSet leafs) throws SQLException {
        this.id = leafs.getString("id");
        this.descr = leafs.getString("descr");
        this.calculationFormula = leafs.getString("calculation");
        this.formulaType = FormulaTypeEnum.valueOf(leafs.getString("formula_type"));
        this.a = leafs.getString("a");
        this.b = leafs.getString("b");
        this.c = leafs.getString("c");
        this.weight = leafs.getDouble("weight");
        this.d = leafs.getString("d");
        this._XB = leafs.getString("xb");
        this.comments = new HashSet<>(Collections.singletonList(leafs.getString("comments")));

    }

    /**
     * Конструктор для промежуточной формулы иерархии
     *
     * @param id                 идентификатор формулы
     * @param descr              описание формулы
     * @param calculationFormula входное значение
     * @param formulaType        тип формулы
     * @param a                  первый параметр
     * @param b                  второй параметр
     * @param c                  третий параметр
     * @param d                  четвертый параметр
     * @param _XB                коэффициент к второму параметру
     * @param weight             вес значения
     * @param comments           комментарии на значение риска
     * @throws WrongFormulaValueException При неверной формуле
     */
    public Formula(String id, String descr, double calculationFormula, String formulaType, String a, String b, String c,
                   String d, String _XB, double weight, Set<String> comments) throws WrongFormulaValueException {
        this.id = id;
        this.descr = descr;
        this.formulaType = FormulaTypeEnum.valueOf(formulaType);
        this.inputValue = calculationFormula;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this._XB = _XB;
        this.weight = weight;
        this.comments = comments;
        this.calculationFormula = null;
    }



    /**
     * Расчет формулы this.formulaType на основе формулы из входной строки
     *
     * @param calcFormula формула со значениями вместо идентификаторов
     * @param inn         инн для расчета
     * @param year        год для расчета
     * @param replacer    объект для замены данных
     * @return результат вычисления нечеткой формулы, умноженный на вес
     * @throws FormulaCalculationException При ошибке расчета формулы
     */
    public FormulaResult calculate(String calcFormula, String inn, int year,
                                   DataReplacer replacer) throws FormulaCalculationException {
        try {
            calcFormula = ValidationUtil.inputCorrection(calcFormula);

            double inputValue = replacer.calculateExpression(calcFormula);
            double a = replacer.calculateAggregates(this.a, inn);
            double b = replacer.calculateAggregates(this.b, inn);
            double c = replacer.calculateAggregates(this.c, inn);
            double d = replacer.calculateAggregates(this.d, inn);
            double xb = replacer.calculateAggregates(this._XB, inn);
            this.result = this.weight * this.getFormulaType().calculate(
                    inputValue, a, b, c, d, xb);
            this.inputValue = inputValue;

            return new FormulaResult(inn, this.id, inputValue, result, year, descr, formulaType,
                    a, b, c, d, xb, comments);

        } catch (Exception ex) {
            throw new FormulaCalculationException("Ошибка при расчете: " + ex);
        }
    }

    /**
     * Обновление параметров формулы
     *
     * @param params новые параметры
     */
    public void updateParams(String... params) {
        this.a = params[0];
        this.b = params[1];
        this.c = params[2];
        this.d = params[3];
        this._XB = params[4];
    }


}
