package ru.mifi.service.risk.domain;


import lombok.Data;
import ru.mifi.service.risk.domain.enums.FormulaTypeEnum;

import java.util.Set;

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
    private Double normalizedResult;
    private double inputeValue;
    private double result;
    private String comment;
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
    public FormulaResult(String inn, String id, double inputeValue, double result, double normalizedResult, int year,
                         FormulaResult formulaResult) {
        this.inn = inn;
        this.id = id;
        this.result = result;
        this.normalizedResult = normalizedResult;
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
     * Конструктор по сокращенному количеству полей
     *
     * @param inn         инн
     * @param id          идентификатор формулы
     * @param inputeValue входное значение
     * @param result      резуальтат
     * @param year        год
     */
    public FormulaResult(String inn, String id, double inputeValue, double result, int year, Set<String> comments) {
        this.inn = inn;
        this.id = id;
        this.result = result;
        this.inputeValue = inputeValue;
        this.year = year;
        this.descr = null;
        this.formulaType = null;
        this.normalizedResult = null;
        this.comment = commentInit(comments);
    }

    /**
     * Конструктор по минимальному количеству полей
     *
     * @param inn  инн
     * @param id   идентификатор формулы
     * @param year год
     */
    public FormulaResult(String inn, String id, int year, String msg) {
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
        this.comment = msg;
    }

    /**
     * Инициализация комментариев формулы на основании результата
     *
     * @param comments комментарии
     * @return комментарий, указанный для получившегося значения риска
     */
    private String commentInit(Set<String> comments) {
        if (comments == null || comments.size() == 0) {
            return "";
        }
        float step = 1f / comments.size();
        for (float border = 0f; border < 1f; border += step) {
            if (result >= border && result <= border + step) {
                return ((String) comments.toArray()[Math.round(border / step)]);
            }
        }
        return "";
    }
}
