package ru.mifi.service.risk.exception;

/**
 * Ошибка при работе с базой.
 * Created by DenRUS on 06.10.2018.
 */
public class ImportException extends RuntimeException{
    public ImportException(String s) {
        super(s);
    }
    public ImportException(String s, Throwable ex) {
        super(s, ex);
    }
}
