package ru.mifi.service.risk.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
    private Integer insertFormulaStmtCounter = 0;
    private Integer insertModelStmtCounter = 0;
    private Integer insertModelCalcStmtCounter = 0;

    final DataSource ds;
    final Connection connection;
    final PreparedStatement insertCompanyStmt;
    final PreparedStatement insertCompanyParamStmt;
    final PreparedStatement insertFormulaStmt;
    final PreparedStatement insertModelStmt;
    final PreparedStatement insertModelCalcStmt;
    private boolean hasErrors = false;

    private static final String SQL_INSERT_COMPANY = "INSERT INTO company (id, inn) VALUES (?,?)";
    private static final String SQL_INSERT_COMPANY_PARAM = "INSERT INTO company_business_params (company_id, param_code, year, param_value) VALUES (?,?,?,?)";
    private static final String SQL_INSERT_FORMULA = "INSERT INTO formula (node, calculation, descr, formula_type, a, b, c, d, xb,comments) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_INSERT_MODEL = "INSERT INTO model (id, descr) VALUES (?,'Загружено из Excel')";
    private static final String SQL_INSERT_MODEL_CALC = "INSERT INTO model_CALC (model_id, node, parent_node, weight, level, is_leaf) VALUES (?,?,?,?,?,?)";



    public DatabaseExcelImportAccessor(DataSource ds) throws SQLException {
        this.ds = ds;
        this.connection = ds.getConnection();
        connection.setAutoCommit(false);
        this.insertCompanyParamStmt = connection.prepareStatement(SQL_INSERT_COMPANY_PARAM);
        this.insertCompanyStmt = connection.prepareStatement(SQL_INSERT_COMPANY);
        this.insertFormulaStmt = connection.prepareStatement(SQL_INSERT_FORMULA);
        this.insertModelStmt = connection.prepareStatement(SQL_INSERT_MODEL);
        this.insertModelCalcStmt = connection.prepareStatement(SQL_INSERT_MODEL_CALC);
    }

    private void tryToClose(AutoCloseable toClose) {
        try {
            toClose.close();
        } catch (Exception ex) {
            LOG.error("Не удалось закрыть: " + insertCompanyParamStmt);
        }
    }
    private void tryToCloseAndExecute(Statement stmt) throws SQLException {
        try {
            stmt.executeBatch();
        } catch (Exception ex) {
          LOG.error("Ошибка при выполнении батча: " + ex.getMessage(), ex);
          hasErrors = true;
        } finally {
            tryToClose(stmt);
        }
    }

    @Override
    public void close() throws Exception {
        tryToCloseAndExecute(insertModelStmt);
        tryToCloseAndExecute(insertModelCalcStmt);
        tryToCloseAndExecute(insertCompanyParamStmt);
        tryToCloseAndExecute(insertCompanyStmt);
        tryToCloseAndExecute(insertFormulaStmt);
        if (hasErrors) {
            connection.rollback();
        } else {
            connection.commit();
        }
        tryToClose(connection);
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
     * Добавляем узел модель расчетов для инсерта
     * @throws SQLException при косяке работы со стейтментами
     */
    public void insertModelCalc(
            String model_id,
            String node,
            String parent_node,
            Double weight,
            Integer level,
            Integer isLeaf
    ) throws SQLException {
        insertModelCalcStmt.setString(1, model_id);
        insertModelCalcStmt.setString(2, node);
        insertModelCalcStmt.setString(3, parent_node);
        insertModelCalcStmt.setDouble(4, weight);
        insertModelCalcStmt.setInt(5, level);
        insertModelCalcStmt.setInt(6, isLeaf);
        insertModelCalcStmtCounter = addReqToStmtBatch(insertModelCalcStmtCounter, insertModelCalcStmt);
    }

    /**
     * Добавляем модель для инсерта
     * @throws SQLException при косяке работы со стейтментами
     */
    public void insertModel(String id) throws SQLException {
        insertModelStmt.setString(1, id);
        insertModelStmtCounter= addReqToStmtBatch(insertModelStmtCounter, insertModelStmt);
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
     * Добавляем формулу для сохранения в базу.
     * @param node имя узла иерархии расчета
     * @param calculation способ подсчета входного значения
     * @param descr описание формулы
     * @param formulaType тип формулы
     * @param a параметр А
     * @param b параметр B
     * @param c параметр C
     * @param d параметр D
     * @param xb параметр XB
     * @throws SQLException если че-то не так с БД
     */
    public void insertFormula(
            String node,
            String calculation,
            String descr,
            String formulaType,
            String a,
            String b,
            String c,
            String d,
            String xb,
            String comments
    ) throws SQLException {
        int counter = 1;
        insertFormulaStmt.setString(counter++, node);
        insertFormulaStmt.setString(counter++, calculation);
        insertFormulaStmt.setString(counter++, descr);
        insertFormulaStmt.setString(counter++, formulaType);
        insertFormulaStmt.setString(counter++, a);
        insertFormulaStmt.setString(counter++, b);
        insertFormulaStmt.setString(counter++, c);
        insertFormulaStmt.setString(counter++, d);
        insertFormulaStmt.setString(counter++, xb);
        insertFormulaStmt.setString(counter, comments);
        insertFormulaStmtCounter = addReqToStmtBatch(insertFormulaStmtCounter, insertFormulaStmt);
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
            try {
                stmt.executeBatch();
            } catch (Exception ex) {
                LOG.error("Ошибка при выполнении батча по достижению лимита запросов: " + ex.getMessage(), ex);
                hasErrors = true;
            }
        }
        return counter;
    }
}
