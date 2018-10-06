package ru.mifi.service.risk.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Запросы на импорт данных из Excel.
 * Created by DenRUS on 06.10.2018.
 */
public class DatabaseExcelImportAccessor implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseExcelImportAccessor.class);
    private static final int BATCH_SIZE = 100;
    private Integer insertCompanyStmtCounter = 0;
    private Integer insertCompanyParamStmtCounter = 0;

    final DataSource ds;
    final Connection connection;
    final PreparedStatement insertCompanyStmt;
    final PreparedStatement insertCompanyParamStmt;

    private static final String SQL_INSERT_COMPANY = "INSERT INTO company (id, inn) VALUES (?,?)";
    private static final String SQL_INSERT_COMPANY_PARAM = "INSERT INTO company_business_params (company_id, param_code, year, param_value) VALUES (?,?,?,?)";



    public DatabaseExcelImportAccessor(DataSource ds) throws SQLException {
        this.ds = ds;
        this.connection = ds.getConnection();
        this.insertCompanyParamStmt = connection.prepareStatement(SQL_INSERT_COMPANY_PARAM);
        this.insertCompanyStmt = connection.prepareStatement(SQL_INSERT_COMPANY);

    }

    @Override
    public void close() throws Exception {
        try {
            insertCompanyParamStmt.executeBatch();
            insertCompanyParamStmt.close();
        } catch (Exception ex) {
            LOG.error("Не удалось закрыть конекшн на инсерт параметров компаний: " + insertCompanyParamStmt);
        } finally {
            try {
                insertCompanyStmt.executeBatch();
                insertCompanyStmt.close();
            } catch (Exception ex) {
                LOG.error("Не удалось закрыть конекшн на инсерт компаний: " + insertCompanyParamStmt);
            } finally {
                connection.close();
            }
        }
    }

    /**
     * Добавляем компанию для инсерта
     * @param inn   инн
     * @return  присвоенный идентификатор
     * @throws SQLException при косяке работы со стейтментами
     */
    public String insertCompany(String inn) throws SQLException {
        String id = UUID.randomUUID().toString();
        insertCompanyStmt.setString(1, id);
        insertCompanyStmt.setString(2, inn);
        insertCompanyStmtCounter = addReqToStmtBatch(insertCompanyStmtCounter, insertCompanyStmt);
        return id;
    }
    /**
     * Добавляем компанию для инсерта
     * @param inn   инн
     * @return  присвоенный идентификатор
     * @throws SQLException при косяке работы со стейтментами
     */
    public String insertCompany(String id, String inn) throws SQLException {
        insertCompanyStmt.setString(1, id);
        insertCompanyStmt.setString(2, inn);
        insertCompanyStmtCounter = addReqToStmtBatch(insertCompanyStmtCounter, insertCompanyStmt);
        return id;
    }

    /**
     * Добавляем параметр компании для сохранения в базу.
     * @param companyId  идентификатор компании
     * @param param_code код параметра
     * @param year       год
     * @param paramValue значение параметра
     */
    public void insertCompanyParam(String companyId, String param_code, Integer year, Double paramValue) throws SQLException {
        insertCompanyParamStmt.setString(1, companyId);
        insertCompanyParamStmt.setString(2, param_code);
        insertCompanyParamStmt.setInt(3, year);
        insertCompanyParamStmt.setDouble(4, paramValue);
        addReqToStmtBatch(insertCompanyParamStmtCounter, insertCompanyParamStmt);
    }

    private Integer addReqToStmtBatch(Integer counter, PreparedStatement stmt) throws SQLException {
        stmt.addBatch();
        stmt.clearParameters();
        if (++counter % 100 == 0) {
            stmt.executeBatch();
        }
        return counter;
    }
}
