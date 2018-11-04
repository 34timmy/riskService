package ru.mifi.service.risk.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.mifi.service.risk.domain.CalculationParamKey;
import ru.mifi.service.risk.exception.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

/**
 * Работа с базой для DDL.
 * Created by DenRUS on 06.10.2018.
 */
@Component
public class DatabaseDdlAccessor {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseDdlAccessor.class);
    private static final String SQL_CREATE_TEMP_TABLE =
            "CREATE TABLE %s (\n" +
                    "   company_id VARCHAR2(255) NOT NULL," +
                    "   node VARCHAR2(255) NOT NULL," +
                    "   parent_node VARCHAR2(255), " +
                    "   weight DOUBLE PRECISION, " +
                    "   is_leaf INTEGER, " +
                    "   comment VARCHAR2(4000), " +
                    "   value DOUBLE " +
                    ")";
    private static final String SQL_PK_TEMP_TABLE_CONSTR = "ALTER TABLE %s ADD CONSTRAINT %s_pk PRIMARY KEY (company_id, node)";
    private static final String SQL_WEIGH_CHECK_TEMP_TABLE_CONSTR = "ALTER TABLE %s ADD CONSTRAINT %s_weight_val_check CHECK (weight BETWEEN 0 and 100)";
    private static final String SQL_LEAF_CHECK_TEMP_TABLE_CONSTR = "ALTER TABLE %s ADD CONSTRAINT %s_leaf_val_check CHECK (is_leaf BETWEEN 0 and 1)";
    private static final String SQL_INSERT_RESULT_INFO = "INSERT INTO result_data_mapper (model_id, company_list_id, table_name) VALUES (?,?,?)";

    /**
     * Создает таблицу для сохранения результатов расчета.
     *
     * @param key  объект-ключ для параметров расчета
     * @param conn подключение к БД
     * @return имя таблицы
     */
    public String createTempTable(
            CalculationParamKey key,
            Connection conn
    ) {
        try (
                Statement prepStmt = conn.createStatement();
        ) {
            String tableName = ("calc_result_"
                    + key.getModelId()
                    + "_" + key.getCompanyListId()
                    + "_" + key.getAllCompaniesListId()
                    + "_" + key.getYear()
                    + "_" + LocalDateTime.now().toString())
                    .replaceAll("[\\-\\:\\.]", "");
            prepStmt.execute(String.format(
                    SQL_CREATE_TEMP_TABLE,
                    tableName
            ));
            prepStmt.execute(String.format(
                    SQL_PK_TEMP_TABLE_CONSTR,
                    tableName,
                    tableName
            ));
            prepStmt.execute(String.format(
                    SQL_WEIGH_CHECK_TEMP_TABLE_CONSTR,
                    tableName,
                    tableName
            ));

            prepStmt.execute(String.format(
                    SQL_LEAF_CHECK_TEMP_TABLE_CONSTR,
                    tableName,
                    tableName
            ));
            return tableName;
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка создания временной таблицы для результата");
        }
    }
}
