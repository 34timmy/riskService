package ru.mifi.service.risk.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mifi.service.risk.domain.enums.FormulaTypeEnum;
import ru.mifi.service.risk.exception.FormulaCalculationException;
import ru.mifi.service.risk.exception.WrongFormulaValueException;
import ru.mifi.service.risk.utils.validation.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ru.mifi.utils.StringUtils.extractComments;

/**
 * Класс-представление формулы
 *
 * @author DenRUS on 07.10.2017.
 */
@Data
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
    private final Double interpretationK;
    private final String nodeName;


    private String id;
    private String descr;
    private FormulaTypeEnum formulaType;
    private String calculationFormula;
    private String a;
    private String b;
    private String c;
    private String d;
    private String _XB;
    private double weight;
    private List<String> comments;
    private double result;
    private double inputValue;
    private String model_calc_id;


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
        this.interpretationK = leafs.getDouble("k_interpret");
        this._XB = leafs.getString("xb");
        this.comments = extractComments(leafs.getString("comments"));
        this.nodeName = leafs.getString("descr");

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
                    a, b, c, d, xb, interpretationK, comments, nodeName);

        } catch (Exception ex) {
            throw new FormulaCalculationException("Ошибка при расчете: " + ex);
        }
    }
}
