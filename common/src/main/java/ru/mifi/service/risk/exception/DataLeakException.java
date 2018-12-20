package ru.mifi.service.risk.exception;

/**
 * Ошибка недостатка данных
 * Created by DenRUS on 08.10.2017.
 */
public class DataLeakException extends Exception {

    public DataLeakException(String mes) {
        super(mes);
    }

    public DataLeakException(String mes, Exception ex) {
        super(mes, ex);
    }


}
