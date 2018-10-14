package ru.mifi.service.risk.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Формула для расчета.
 * Created by DenRUS on 08.10.2018.
 */
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

    public Formula(String node, String descr, String calculation, String formulaType, String a, String b, String c, String d, String xb, String comments) {
        this.node = node;
        this.descr = descr;
        this.calculation = calculation;
        this.formulaType = formulaType;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.xb = xb;
        this.comments = comments;
    }

    public String getNode() {
        return node;
    }

    public String getDescr() {
        return descr;
    }

    public String getCalculation() {
        return calculation;
    }

    public String getFormulaType() {
        return formulaType;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public String getXb() {
        return xb;
    }

    public String getComments() {
        return comments;
    }

    String node;
    String descr;
    String calculation;
    String formulaType;
    String a;
    String b;
    String c;
    String d;
    String xb;
    String comments;

    public Formula(ResultSet leafs) throws SQLException {
        this.node = leafs.getString("node");
        this.descr = leafs.getString(descr);
        this.calculation = leafs.getString(calculation);
        this.formulaType = leafs.getString(formulaType);
        this.a = leafs.getString(a);
        this.b = leafs.getString(b);
        this.c = leafs.getString(c);
        this.d = leafs.getString(d);
        this.xb = leafs.getString(xb);
        this.comments = leafs.getString(comments);

    }
}
