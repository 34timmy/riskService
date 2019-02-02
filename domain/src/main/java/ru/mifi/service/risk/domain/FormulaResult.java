package ru.mifi.service.risk.domain;


import lombok.Data;
import ru.mifi.service.risk.domain.enums.FormulaTypeEnum;

import java.util.List;

/**
 * Created by DenRUS on 19.10.2017.
 */
@Data
public class FormulaResult {
    private final String inn;
    private final String id;
    private final int year;
    private final String descr;
    private final FormulaTypeEnum formulaType;
    private List<String> allComments;
    private Double normalizedResult;
    private Double lineadResult;
    private double inputeValue;
    private double result;
    private String comment;
    private double interpretationValue;
    private double interpretationK;
    private double a;
    private double b;
    private double c;
    private double d;
    private double _XB;
    private double inputValue;
    private final String nodeName;

    /**
     * Конструктор по всем полям
     *
     * @param inn         инн
     * @param id          идентификатор формулы
     * @param inputeValue входное значение
     * @param result      результат
     * @param year        год
     * @param descr       описание формулы
     * @param formulaType тип формулы
     * @param a           параметр а формулы
     * @param b           параметр b формулы
     * @param c           параметр c формулы
     * @param d           параметр d формулы
     * @param xb          параметр xb формулы
     * @param comments    комментарии к формуле
     */
    public FormulaResult(String inn, String id, double inputeValue, double result, int year,
                         String descr, FormulaTypeEnum formulaType, double a, double b, double c, double d, double xb,
                         double interpretationK, List<String> comments, String nodeName) {
        this.inn = inn;
        this.id = id;
        this.result = result;
        this.inputeValue = inputeValue;
        this.year = year;
        this.descr = descr;
        this.formulaType = formulaType;
        this.interpretationK = interpretationK;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this._XB = xb;
        this.allComments = comments;
        this.nodeName = nodeName;
    }

    /**
     * Конструктор на основании другого результата. Для расчета иерархии
     *
     * @param inn           инн
     * @param id            идентификатор формулы
     * @param inputeValue   входное значение
     * @param result        результат (риск)
     * @param year          год
     * @param formulaResult результат формулы-родитель
     */
    public FormulaResult(String inn, String id, double inputeValue, double result, double normalizedResult, int year,
                         double lineadResult, double interpretationValue, double interpretationK, FormulaResult formulaResult) {
        this.inn = inn;
        this.id = id;
        this.result = result;
        this.normalizedResult = normalizedResult;
        this.lineadResult = lineadResult;
        this.inputeValue = inputeValue;
        this.year = year;
        this.descr = formulaResult.descr;
        this.formulaType = formulaResult.formulaType;
        this.a = formulaResult.a;
        this.b = formulaResult.b;
        this.c = formulaResult.c;
        this.d = formulaResult.d;
        this._XB = formulaResult._XB;
        this.interpretationK = interpretationK;
        this.interpretationValue = interpretationValue;
        this.comment = commentInit(formulaResult.getAllComments(), interpretationValue);
        this.nodeName = formulaResult.getNodeName();
    }

    /**
     * Конструктор по сокращенному количеству полей
     *
     * @param inn         инн
     * @param id          идентификатор формулы
     * @param inputeValue входное значение
     * @param result      резуальтат
     * @param year        год
     */
    public FormulaResult(String inn, String id, double inputeValue, double result, int year, String nodeName, List<String> comments) {
        this.inn = inn;
        this.id = id;
        this.result = result;
        this.nodeName = nodeName;
        this.inputeValue = inputeValue;
        this.year = year;
        this.descr = null;
        this.formulaType = null;
        this.normalizedResult = null;
        this.lineadResult = null;
        this.allComments = comments;
    }

    /**
     * Конструктор по минимальному количеству полей - для ошибочных формул
     *
     * @param inn  инн
     * @param id   идентификатор формулы
     * @param year год
     */
    public FormulaResult(String inn, String id, int year, String nodeName, String msg) {
        this.inn = inn;
        this.id = id;
        this.result = 0;
        this.inputeValue = 0;
        this.year = year;
        this.descr = null;
        this.formulaType = null;
        this.nodeName = nodeName;
        this.a = 0;
        this.b = 0;
        this.c = 0;
        this.d = 0;
        this._XB = 0;
        this.comment = msg;
    }

    /**
     * Инициализация комментариев формулы на основании результата
     *
     * @param comments комментарии
     * @return комментарий, указанный для получившегося значения риска
     */
    private String commentInit(List<String> comments, double interpretationValue) {
        if (comments == null || comments.size() == 0) {
            return "";
        }
        if (comments.size() != 3) {
            throw new IllegalArgumentException("Неверное количество комментариев!");
        }
        if (interpretationValue <= ((double) 1 / 3)) {
            return comments.get(0);
        }
        if (interpretationValue > ((double) 1 / 3) && interpretationValue <= ((double) 2 / 3)) {
            return comments.get(1);
        }
        if (interpretationValue > ((double) 2 / 3) && interpretationValue <= ((double) 1)) {
            return comments.get(2);
        }
        return "Интерпретационное значение больше 1";
//        float step = 1f / comments.size();
//        for (float border = 0f; border < 1f; border += step) {
//            if (result >= border && result <= border + step) {
//                return ((String) comments.toArray()[Math.round(border / step)]);
//            }
//        }
    }

    /**
     * Инициализируем свои комментарии по своим же значениям.
     */
    public void selfCommentInit() {
        this.comment = this.commentInit(this.allComments, this.interpretationValue);
    }
}
