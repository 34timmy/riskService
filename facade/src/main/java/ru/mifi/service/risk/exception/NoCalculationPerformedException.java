package ru.mifi.service.risk.exception;

import ru.mifi.service.risk.domain.CalculationParamKey;

/**
 * Ошибка недостатка данных
 * Created by DenRUS on 08.10.2017.
 */
public class NoCalculationPerformedException extends RuntimeException {

    public NoCalculationPerformedException(CalculationParamKey key) {
        super("Нет данных с расчетами по параметрам: " + key);
    }

    public NoCalculationPerformedException(CalculationParamKey key, Exception ex) {
        super("Нет данных с расчетами по параметрам: " + key, ex);
    }


}
