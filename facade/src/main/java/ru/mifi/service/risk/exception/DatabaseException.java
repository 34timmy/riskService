package ru.mifi.service.risk.exception;

/**
 * Ошибка при работе с базой.
 * Created by DenRUS on 06.10.2018.
 */
public class DatabaseException extends RuntimeException{
    public DatabaseException(String s) {
        super(s);
    }
    public DatabaseException(String s, Throwable ex) {
        super(s, ex);
    }
}
