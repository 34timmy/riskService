package ru.mifi.service.risk.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;

/**
 * Класс-отображение для результата иерархии
 *
 * @author DenRUS on 19.10.2017.
 */
@Getter
@AllArgsConstructor
public class HierarchyResult {
    /**
     * Результат вычисления формул по всем компаниям, без сбора по иерархии
     */
    private final Map<String, Map<String, FormulaResult>> calculationData;
    /**
     * Результат вычисления значений после сбора по иерархии
     */
    private final Map<String, Collection<FormulaResult>> hierarchyData;
    /**
     * Упрощенное представление формул
     */
    private final Collection<FormulaResult> simpleFormulasData;

}
