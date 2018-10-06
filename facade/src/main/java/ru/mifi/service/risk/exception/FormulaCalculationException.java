package ru.mifi.service.risk.exception;

/**
 * Ошибка при расчете формулы
 * Created by DenRUS on 08.10.2017.
 */
public class FormulaCalculationException extends Exception {

    public FormulaCalculationException(String mes) {
        super(mes);
    }

    public FormulaCalculationException(String mes, Exception ex) {
        super(mes, ex);
    }


}
