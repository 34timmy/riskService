package ru.mifi.service.risk.utils;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.mifi.service.risk.database.DatabaseCalculationAccessorImpl;
import ru.mifi.service.risk.database.DatabaseSelectAccessor;
import ru.mifi.service.risk.domain.CalculationParamKey;
import ru.mifi.service.risk.domain.DataKey;
import ru.mifi.service.risk.domain.Formula;
import ru.mifi.service.risk.domain.HierarchyResult;
import ru.mifi.service.risk.dto.CalcResultDto;
import ru.mifi.service.risk.exception.DatabaseException;
import ru.mifi.service.risk.exception.NoCalculationPerformedException;
import ru.mifi.service.risk.utils.old.CalculationService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Основная логика по расчету.
 * Created by DenRUS on 06.10.2018.
 */
@Component
@AllArgsConstructor
public class DataService {

    private static final Logger LOG = LoggerFactory.getLogger(DataService.class);

    private final DataSource dataSource;
    private final DatabaseSelectAccessor accessor;

    /**
     * Основной метод расчета.
     *
     * @param calcKey Объект с параметрами расчета
     * @return надо подумать: либо id таблицы с данными, либо сообщение-статус расчета.
     */
    public Map<String, Object> performCalculation(CalculationParamKey calcKey, String user) {
        try (DatabaseCalculationAccessorImpl accessor = new DatabaseCalculationAccessorImpl(dataSource)) {
            HierarchyResult finalResult;
            {
                Set<Formula> leafs = accessor.getFormulasForCalc(calcKey.getModelId());
                Set<String> companies = accessor.getCompanyIdsByListId(calcKey.getCompanyListId());
                Set<String> allCompanies = accessor.getExtendedCompanyList(calcKey);

                Map<DataKey, Map<String, Double>> params = accessor.getParamsForFormula(
                        leafs.stream()
                                .map(Formula::getId)
                                .collect(Collectors.toSet()),
                        Stream.of(companies, allCompanies)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toSet()),
                        calcKey.getYear());
                LOG.info("Данные получены. Начинаем расчет формул.");
                CalculationService calculationService = new CalculationService(
                        companies,
                        allCompanies,
                        leafs,
                        params,
                        accessor,
                        calcKey.getModelId()
                );
                LOG.info("Формулы расчитаны. Начинаем расчет иерархии.");
                finalResult = calculationService.calculateFormulasHierarchy(
                        calcKey.getYear()
                );
                LOG.info("Расчет завершен.");
            }
            String tableName = accessor.saveDataToDb(finalResult, calcKey, user);
            return getResultAsMap(tableName, "Расчет проведен успешно");
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка БД при расчете иерархии: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при расчете иерархии: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> getResultAsMap(String tableName, String str) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", str);
        result.put("tableName", tableName);
        return result;
    }

    /**
     * Загружаем результат из БД, кладем его в {@link ru.mifi.service.risk.dto.CalcResultDto} и отдаем
     *
     * @param paramKey Объект с параметрами для расчета
     * @return результат иерархии расчета
     */
    public Map<String, CalcResultDto> getCalculationResultFromDb(
            CalculationParamKey paramKey
    ) {
        Set<String> tables = accessor.getTablesForModelAndList(paramKey);
        String lastTable = tables.stream()
                .sorted()
                .findFirst()
                .orElseThrow(() -> new NoCalculationPerformedException(paramKey));
        return accessor.getDataFromTable(lastTable);
    }
}
