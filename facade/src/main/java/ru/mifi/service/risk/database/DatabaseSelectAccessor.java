package ru.mifi.service.risk.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mifi.service.risk.domain.CalculationParamKey;
import ru.mifi.service.risk.dto.CalcResultDto;
import ru.mifi.service.risk.exception.DatabaseException;
import ru.mifi.service.risk.utils.ExcelLoader;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Основная логика по работе с БД.
 * Created by DenRUS on 06.10.2018.
 */
@Component
public class DatabaseSelectAccessor {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseSelectAccessor.class);
    private static final String SQL_GET_RES_TABLES_NAMES = "SELECT table_name FROM result_data_mapper WHERE model_id = ? AND company_list_id = ? AND all_company_list_id = ? AND year = ?";
    private static final String SQL_GET_RES_FROM_TABLE =
            "SELECT " +
                    "   company_id, node, parent_node, weight, is_leaf, comment, value " +
                    "FROM " +
                    "   %s";//TODO пагинация и сортировка
    private DataSource dataSource;

    @Autowired
    public DatabaseSelectAccessor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Лезем в таблицу с маппингом и достаем таблицы с результатами расчетов по выбранным модели и листу компаний.
     *
     * @param key сущность с необходимыми параметрами для расчета
     * @return список таблиц с результатом
     */
    public Set<String> getTablesForModelAndList(
            CalculationParamKey key
    ) {
        Set<String> tableNames = new HashSet<>();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_RES_TABLES_NAMES)
        ) {
            stmt.setString(1, key.getModelId());
            stmt.setString(2, key.getCompanyListId());
            stmt.setString(3, key.getAllCompaniesListId());
            stmt.setInt(4, key.getYear());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                tableNames.add(res.getString(1));
            }
            return tableNames;
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при получении имен таблиц с результатами");
        }
    }

    /**
     * Получаем результат из уже известной таблицы.
     *
     * @param tableName имя таблицы
     * @return результаты расчета
     */
    public Map<String, CalcResultDto> getDataFromTable(String tableName) {
        Set<CalcResultDto> dtos = new HashSet<>();
        try (
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            ResultSet res = stmt.executeQuery(String.format(SQL_GET_RES_FROM_TABLE, tableName));
            while (res.next()) {
                dtos.add(new CalcResultDto(res));
            }
            return CalcResultDto.setLinksAmongDtos(dtos);
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка БД при получении имен таблиц с результатами", e);
        } catch (Exception ex) {
            throw new DatabaseException("Ошибка при получении имен таблиц с результатами", ex);
        }
    }

    public Set<String> getTableNamesForCalcResult() {
        Set<String> tableNames = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                String tableName;
                if ((tableName = rs.getString(3)).startsWith("calc_result")) {
                    tableNames.add(tableName);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка БД при получении имен таблиц с результатами", e);
        }
        return tableNames;
    }
}
