package ru.mifi.service.risk.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mifi.service.risk.exception.DatabaseException;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

/**
 * Работа с базой для DDL.
 * Created by DenRUS on 06.10.2018.
 */
public class DatabaseDdlAccessor {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseDdlAccessor.class);

    public DatabaseDdlAccessor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private DataSource dataSource;
    
    private static final String SQL_CREATE_TEMP_TABLE =
            "CREATE TABLE %s (\n" +
                    "        CREATE TABLE  || table_name ||  (\n" +
                    "               node VARCHAR2(255) NOT NULL," +
                    "               parent_node VARCHAR2(255), " +
                    "               weight INTEGER, " +
                    "               is_leaf INTEGER, " +
                    "               value DOUBLE, " +
                    "        )";
    private static final String SQL_PK_TEMP_TABLE_CONSTR = "ALTER TABLE %s ADD CONSTRAINT %s_pk PRIMARY KEY (node)";
    private static final String SQL_WEIGH_CHECK_TEMP_TABLE_CONSTR = "ALTER TABLE %s ADD CONSTRAINT %s_weight_val_check CHECK (weight BETWEEN 0 and 100)";
    private static final String SQL_LEAF_CHECK_TEMP_TABLE_CONSTR = "ALTER TABLE %s ADD CONSTRAINT %s_leaf_val_check CHECK (is_leaf BETWEEN 0 and 1)";
    private static final String SQL_INSERT_RESULT_INFO = "INSERT INTO result_data_mapper (model_id, company_list_id, table_name) VALUES (?,?,?)";

//    @Resource(name="dataSource")
//    public void setDataSource(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
    
    public void createTempTable(String modelId, String companiesListId) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement prepStmt = conn.prepareStatement(SQL_INSERT_RESULT_INFO)
        ) {
            String tableName = "calc_result_" + modelId + "_" + companiesListId + "_" + LocalDateTime.now().toString();
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
            prepStmt.setString(1, modelId);
            prepStmt.setString(2, companiesListId);
            prepStmt.setString(3, tableName);
            prepStmt.executeQuery();
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка создания временной таблицы для результата");
        }
    }
}
