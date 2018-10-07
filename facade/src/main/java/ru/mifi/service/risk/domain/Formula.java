package ru.mifi.service.risk.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Формула для расчета.
 * Created by DenRUS on 08.10.2018.
 */
public class Formula {
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
