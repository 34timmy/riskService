package ru.mifi.service.risk.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mifi.service.risk.dto.CalcResultDto;
import ru.mifi.service.risk.exception.DatabaseException;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Основная логика по работе с БД.
 * Created by DenRUS on 06.10.2018.
 */
public class DatabaseSelectAccessor {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseSelectAccessor.class);

    public DatabaseSelectAccessor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private DataSource dataSource;

    private static final String SQL_GET_RES_TABLES_NAMES = "SELECT table_name FROM result_data_mapper WHERE model_id = ? AND company_list_id = ?";
    private static final String SQL_GET_RES_FROM_TABLE = "SELECT * FROM %s";//TODO пагинация и сортировка

    /**
     * Лезем в таблицу с маппингом и достаем таблицы с результатами расчетов по выбранным модели и листу компаний.
     *
     * @param modelId       идентификатор модели
     * @param companyListId идентификатор листа компаний
     * @return список таблиц с результатом
     */
    public Set<String> getTablesForModelAndList (String modelId, String companyListId) {
        Set<String> tableNames = new HashSet<>();
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(SQL_GET_RES_TABLES_NAMES)) {
            stmt.setString(1, modelId);
            stmt.setString(2, companyListId);
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
    public Set<CalcResultDto> getDataFromTable (String tableName) {
        Set<CalcResultDto> dtos = new HashSet<>();
        try (Statement stmt = dataSource.getConnection().createStatement()) {
            ResultSet res = stmt.executeQuery(String.format(SQL_GET_RES_FROM_TABLE, tableName));
            while (res.next()) {
                dtos.add(new CalcResultDto(res));
            }
            return dtos;
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при получении имен таблиц с результатами");
        }
    }
}
