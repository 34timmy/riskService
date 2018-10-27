package ru.mifi.service.risk.domain.enums;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Перечисление типов агрегатов с их алгоритмами расчетов
 * @author DenRUS on 20.10.2017.
 */
public enum AggregateTypeEnum {

    MIN("MIN") {
        @Override
        public double calculate(List<Double> results) {
            return results.stream().min(Double::compareTo).get();
        }
    },
    MAX("MAX") {
        @Override
        public double calculate(List<Double> results) {
            return results.stream().max(Double::compareTo).get();
        }
    },
    AVG("AVG") {
        @Override
        public double calculate(List<Double> results) {
            return results.stream().mapToDouble(a -> a).average().getAsDouble();
        }
    },
    MED("MED") {
        @Override
        public double calculate(List<Double> results) {
            List<Double> dataList = results.stream()
                    .sorted(Double::compareTo)
                    .collect(Collectors.toCollection(LinkedList::new));
            return dataList.get(Math.round(dataList.size() / 2));
        }
    };
    private String name;

    AggregateTypeEnum(String name) {
        this.name = name;
    }

    /**
     * Метод расчета конкретного агрегата
     * @param results   результаты по всем ИНН для расчета
     * @return          значение агрегата
     */
    public abstract double calculate(List<Double> results);

    public String getName() {
        return name;
    }
}
