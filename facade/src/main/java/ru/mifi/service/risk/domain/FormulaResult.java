package ru.mifi.service.risk.domain;


import ru.mifi.service.risk.domain.enums.FormulaTypeEnum;

import java.util.Set;

/**
 * Created by DenRUS on 19.10.2017.
 */
public class FormulaResult {
    private final String inn;
    private final String id;
    private double inputeValue;
    private double result;
    private final int year;
    private String comment;

    private final String descr;
    private final FormulaTypeEnum formulaType;
    private double a;
    private double b;
    private double c;
    private double d;
    private double _XB;
    private double inputValue;

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
                         String descr, FormulaTypeEnum formulaType, double a, double b, double c, double d, double xb, Set<String> comments) {
        this.inn = inn;
        this.id = id;
        this.result = result;
        this.inputeValue = inputeValue;
        this.year = year;
        this.descr = descr;
        this.formulaType = formulaType;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this._XB = xb;
        this.comment = commentInit(comments);
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
    public FormulaResult(String inn, String id, double inputeValue, double result, int year,
                         FormulaResult formulaResult) {
        this.inn = inn;
        this.id = id;
        this.result = result;
        this.inputeValue = inputeValue;
        this.year = year;
        this.descr = formulaResult.descr;
        this.formulaType = formulaResult.formulaType;
        this.a = formulaResult.a;
        this.b = formulaResult.b;
        this.c = formulaResult.c;
        this.d = formulaResult.d;
        this._XB = formulaResult._XB;
        this.comment = formulaResult.comment;
    }

    /**
     * Конструктор на основании другого результата
     *
     * @param formulaResult результат формулы - родитель
     * @param a             параметр а формулы
     * @param b             параметр b формулы
     * @param c             параметр c формулы
     * @param d             параметр d формулы
     * @param _xb           параметр xb формулы
     */
    public FormulaResult(FormulaResult formulaResult, double a, double b, double c, double d, double _xb) {
        this.inn = formulaResult.getInn();
        this.id = formulaResult.getId();
        this.result = formulaResult.getResult();
        this.inputeValue = formulaResult.getInputeValue();
        this.year = formulaResult.getYear();
        this.descr = null;
        this.formulaType = formulaResult.getFormulaType();
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this._XB = _xb;
        this.comment = "Сообщение: Формула-агрегат, комментарий не задан.";
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
    public FormulaResult(String inn, String id, double inputeValue, double result, int year) {
        this.inn = inn;
        this.id = id;
        this.result = result;
        this.inputeValue = inputeValue;
        this.year = year;
        this.descr = null;
        this.formulaType = null;
        this.a = 0;
        this.b = 0;
        this.c = 0;
        this.d = 0;
        this._XB = 0;
        this.comment = "Сообщение: Формула-агрегат, комментарий не задан.";
    }

    /**
     * Конструктор по минимальному количеству полей
     *
     * @param inn  инн
     * @param id   идентификатор формулы
     * @param year год
     */
    public FormulaResult(String inn, String id, int year) {
        this.inn = inn;
        this.id = id;
        this.result = 0;
        this.inputeValue = 0;
        this.year = year;
        this.descr = null;
        this.formulaType = null;
        this.a = 0;
        this.b = 0;
        this.c = 0;
        this.d = 0;
        this._XB = 0;
        this.comment = "Сообщение: Формула-агрегат, комментарий не задан.";
    }

    /**
     * Инициализация комментариев формулы на основании результата
     *
     * @param comments комментарии
     * @return комментарий, указанный для получившегося значения риска
     */
    private String commentInit(Set<String> comments) {
        if (comments.size() != 0) {
            float step = 1f / comments.size();
            for (float border = 0f; border < 1f; border += step) {
                if (result >= border && result <= border + step) {
                    return ((String) comments.toArray()[Math.round(border / step)]);
                }
            }
        }
        return "";
    }

    public String getInn() {
        return inn;
    }

    public double getInputeValue() {
        return inputeValue;
    }

    public double getResult() {
        return result;
    }

    public String getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescr() {
        return descr;
    }

    public FormulaTypeEnum getFormulaType() {
        return formulaType;
    }

    //Геттеры
    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    public double get_XB() {
        return _XB;
    }


}
