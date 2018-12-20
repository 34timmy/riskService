package ru.mifi.service.risk.utils.type;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Типы параметров на вход - как правильно из строки для расчета получать параметры, там используемые.
 * Created by DenRUS on 02.11.2018.
 */
public enum ParamsTypeEnum {
    SB {
        @Override
        public List<String> parseCalculationToParams(String calculation) {
            if (calculation == null) {
                return null;
            }
            return Arrays.stream(calculation.toUpperCase().replaceAll("[\\(\\)]", "").split("[\\+\\-\\*\\/]"))
                    .map(String::trim)
                    .filter(str -> str.startsWith("SB_"))
                    .filter(str -> str.endsWith("_T") || str.endsWith("_C") || str.endsWith("_P"))
                    .distinct()
                    .collect(Collectors.toList());
        }
    };

    public abstract List<String> parseCalculationToParams(String calculation);
}
