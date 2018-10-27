package ru.mifi.service.risk.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mifi.service.risk.database.DatabaseCalculationAccessor;
import ru.mifi.service.risk.domain.*;
import ru.mifi.service.risk.exception.DatabaseException;
import ru.mifi.service.risk.utils.old.CalculationService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
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

    /**
     * Основной метод расчета.
     *
     * @param modelId       идентификатор модели
     * @param companyListId идентификатор листа компаний
     * @param year          год расчета
     * @return надо подумать: либо id таблицы с данными, либо сообщение-статус расчета.
     */
    public String performCalculation(String modelId, String companyListId, String allIndustryCompanies, Integer year) {
        try (DatabaseCalculationAccessor accessor = new DatabaseCalculationAccessor(dataSource)) {
            HierarchyResult finalResult;
            {
                Set<Formula> leafs = accessor.getFormulasForCalc(modelId);
                Set<String> companies = accessor.getCompanyIdsByListId(companyListId);
                Set<String> allCompanies = accessor.getCompanyIdsByListId(allIndustryCompanies);

                Map<DataKey, Map<String, Double>> params = accessor.getParamsForFormula(
                        leafs.stream()
                                .map(Formula::getId)
                                .collect(Collectors.toSet()),
                        companies,
                        year);
                CalculationService calculationService = new CalculationService(
                        companies,
                        allCompanies,
                        leafs,
                        params
                );
                finalResult = calculationService.calculateFormulasHierarchy(
                        year
                );
            }

            return "Данные: " + finalResult.toString();
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка БД при расчете иерархии: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при расчете иерархии: " + e.getMessage(), e);
        }
    }

    private Set<String> getAllIndustryCompanies(String allIndustryCompanies) {
        return Arrays.stream(allIndustryCompanies.split(";")).collect(Collectors.toSet());
    }
}
