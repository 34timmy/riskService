package ru.mifi.service.risk.utils;

import ru.mifi.service.risk.database.DatabaseCalculationAccessor;
import ru.mifi.service.risk.domain.CompanyParam;
import ru.mifi.service.risk.domain.Formula;
import ru.mifi.service.risk.domain.FormulaResult;
import ru.mifi.service.risk.exception.DatabaseException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Основная логика по расчету.
 * Created by DenRUS on 06.10.2018.
 */
public class DataService {
    /**
     * Основной метод расчета.
     *
     * @param modelId       идентификатор модели
     * @param companyListId идентификатор листа компаний
     * @param dataSource    датасорс для доступа
     * @return надо подумать: либо id таблицы с данными, либо сообщение-статус расчета.
     */
    public String performCalculation(String modelId, String companyListId, DataSource dataSource) {
        try (DatabaseCalculationAccessor accessor = new DatabaseCalculationAccessor(dataSource)) {
            Map<String, Set<FormulaResult>> results = new HashMap<>();
            {
                Set<Formula> leafs = accessor.getFormulasForCalc(modelId);
                Set<String> companies = accessor.getCompanyIdsByListId(companyListId);

                for (Formula leaf : leafs) {
                    Set<CompanyParam> params = accessor.getParamsForFormula(leaf.getNode(), companies);
                    results.putAll(calculateFormula(leaf, params));
                }
            }
            Set<FormulaResult> finalResult = calculateHierarchy(results, modelId);
            return "Данные: " + finalResult.stream()
                    .map(FormulaResult::asFinalResult)
                    .collect(Collectors.joining("\n"));
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка БД при расчете иерархии: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при расчете иерархии: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @param results результаты листьев
     * @param modelId идентификатор модели
     * @return "верхние" элементы - финальные значения для всех компаний
     */
    private Set<FormulaResult> calculateHierarchy(Map<String, Set<FormulaResult>> results, String modelId) {
        //TODO: сбор по иерархии
        //TODO: сохранение результатов в БД
        //TODO: выдача результата - верхних элементов
        String tableName = null;//TODO
//        results.entrySet().parallelStream(
        return new HashSet<>();
    }


    /**
     *
     * @param formula
     * @param params
     * @return <CompanyId, Set<FormulaResult для этой компании>>
     */
    private Map<String, Set<FormulaResult>> calculateFormula(Formula formula, Set<CompanyParam> params) {
        //TODO: расчет агрегатов, параметров
        //TODO: расчет формулы для всех входных ИНН
        return new HashMap<>();
    }
}
