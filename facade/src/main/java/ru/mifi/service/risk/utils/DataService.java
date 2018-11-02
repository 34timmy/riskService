package ru.mifi.service.risk.utils;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.mifi.service.risk.database.DatabaseCalculationAccessor;
import ru.mifi.service.risk.domain.*;
import ru.mifi.service.risk.exception.DatabaseException;
import ru.mifi.service.risk.utils.old.CalculationService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Основная логика по расчету.
 * Created by DenRUS on 06.10.2018.
 */
@Component
@AllArgsConstructor
public class DataService {

    private final DataSource dataSource;

    private static final Logger LOG = LoggerFactory.getLogger(DataService.class);

    /**
     * Основной метод расчета.
     *
     * @param modelId       идентификатор модели
     * @param companyListId идентификатор листа компаний
     * @param year          год расчета
     * @return надо подумать: либо id таблицы с данными, либо сообщение-статус расчета.
     */
    public Map<String, Object> performCalculation(String modelId, String companyListId, String allIndustryCompaniesListId, Integer year) {
        try (DatabaseCalculationAccessor accessor = new DatabaseCalculationAccessor(dataSource)) {
            HierarchyResult finalResult;
            {
                Set<Formula> leafs = accessor.getFormulasForCalc(modelId);
                Set<String> companies = accessor.getCompanyIdsByListId(companyListId);
                Set<String> allCompanies = accessor.getCompanyIdsByListId(allIndustryCompaniesListId);

                Map<DataKey, Map<String, Double>> params = accessor.getParamsForFormula(
                        leafs.stream()
                                .map(Formula::getId)
                                .collect(Collectors.toSet()),
                        companies,
                        year);
                LOG.info("Данные получены. Начинаем расчет формул.");
                CalculationService calculationService = new CalculationService(
                        companies,
                        allCompanies,
                        leafs,
                        params,
                        accessor,
                        modelId
                );
                LOG.info("Формулы расчитаны. Начинаем расчет иерархии.");
                finalResult = calculationService.calculateFormulasHierarchy(
                        year
                );
                LOG.info("Расчет завершен.");
            }
            String tableName = accessor.saveDataToDb(finalResult, modelId, companyListId, allIndustryCompaniesListId);
            return getResultAsMap(tableName, "Расчет проведен успешно");
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка БД при расчете иерархии: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при расчете иерархии: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> getResultAsMap(String tableName, String s) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", s);
        result.put("tableName", tableName);
        return result;
    }
}
