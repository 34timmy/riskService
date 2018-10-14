package ru.mifi.service.risk.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Формула для расчета.
 * Created by DenRUS on 08.10.2018.
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

    public Formula(String node, String name, String calculation, String formulaType, String a, String b, String c, String d, String xb, String comments) {
        this.node = node;
        this.name = name;
        this.calculation = calculation;
        this.formulaType = formulaType;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.xb = xb;
        this.comments = comments;
    }


    String node;
    String name;
    String calculation;
    String formulaType;
    String a;
    String b;
    String c;
    String d;
    String xb;
    String comments;
    String rule_id;

    public Formula(ResultSet leafs) throws SQLException {
        this.node = leafs.getString("node");
        this.name = leafs.getString(name);
        this.calculation = leafs.getString(calculation);
        this.formulaType = leafs.getString(formulaType);
        this.a = leafs.getString(a);
        this.b = leafs.getString(b);
        this.c = leafs.getString(c);
        this.d = leafs.getString(d);
        this.xb = leafs.getString(xb);
        this.comments = leafs.getString(comments);
    }

    public Formula(String node, String name,
                   String calculation, String formulaType,
                   String a, String b, String c, String d, String xb,
                   String comments, String rule_id) {
        this.node = node;
        this.name = name;
        this.calculation = calculation;
        this.formulaType = formulaType;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.xb = xb;
        this.comments = comments;
        this.rule_id = rule_id;
    }

}
