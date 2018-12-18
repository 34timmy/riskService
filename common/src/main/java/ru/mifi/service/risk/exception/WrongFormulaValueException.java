package ru.mifi.service.risk.exception;

/**
 * Ошибка в синтаксисе формулы
 * Created by DenRUS on 08.10.2017.
 */
public class WrongFormulaValueException extends RuntimeException {

    public WrongFormulaValueException(String mes) {
        super(mes);
    }


}
