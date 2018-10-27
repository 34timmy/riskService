package ru.mifi.constructor.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mifi.service.risk.domain.Formula;

import java.util.Set;

/**
 * Формула для расчета.
 * Created by DenRUS on 08.10.2018.
 */
@Data
@NoArgsConstructor
public class FormulaDTO {
    String id;
    String name;
    String calculation;
    String formulaType;
    String a;
    String b;
    String c;
    String d;
    String xb;
    Set<String> comments;
    String rule_id;

    public FormulaDTO(String id, String name,
                      String calculation, String formulaType,
                      String a, String b, String c, String d, String xb,
                      Set<String> comments) {
        this.id = id;
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

    public FormulaDTO(Formula formula) {
        this.id = formula.getId();
        this.name = formula.getDescr();
        this.calculation = formula.getCalculationFormula();
        this.formulaType = formula.getFormulaType().name();
        this.a = formula.getA();
        this.b = formula.getB();
        this.c = formula.getC();
        this.d = formula.getD();
        this.xb = formula.get_XB();
        this.comments = formula.getComments();

    }
}
