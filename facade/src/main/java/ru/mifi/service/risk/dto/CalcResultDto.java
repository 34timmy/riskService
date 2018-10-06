package ru.mifi.service.risk.dto;

import java.sql.ResultSet;

/**
 * Класс с результами расчета, к оторый отдаем на клиент
 * Created by DenRUS on 06.10.2018.
 */
public class CalcResultDto {
    //TODO: поля для выдачи
    public CalcResultDto(ResultSet resultSet) {
        //TODO: заполнение полей из resultSet`a
    }

    @Override
    public String toString() {
        //TODO: в json-строку
        return null;
    }
}
