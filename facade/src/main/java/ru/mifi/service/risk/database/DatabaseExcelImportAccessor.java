package ru.mifi.service.risk.database;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

/**
 * Запросы на импорт данных из Excel.
 * Created by DenRUS on 06.10.2018.
 */
public class DatabaseExcelImportAccessor extends CustomAutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseExcelImportAccessor.class);
    private static final int BATCH_SIZE = 100;
    private Integer insertCompanyStmtCounter = 0;
    private Integer insertCompanyParamStmtCounter = 0;
    private Integer insertFormulaStmtCounter = 0;
    private Integer insertModelStmtCounter = 0;
    private Integer insertModelCalcStmtCounter = 0;
    private Integer insertFormulaParamStmtCounter = 0;
    final DataSource ds;

    private final Connection connection;
    private final PreparedStatement insertCompanyStmt;
    private final PreparedStatement insertCompanyParamStmt;
    private final PreparedStatement insertFormulaStmt;
    private final PreparedStatement insertModelStmt;
    private final PreparedStatement insertModelCalcStmt;
    private final PreparedStatement insertFormulaParamStmt;

    private static final String SQL_INSERT_COMPANY = "INSERT INTO company (id, inn) VALUES (?,?)";
    private static final String SQL_INSERT_COMPANY_PARAM = "INSERT INTO company_business_params (company_id, param_code, year, param_value) VALUES (?,?,?,?)";
    private static final String SQL_INSERT_FORMULA = "INSERT INTO formula (id, calculation, descr, formula_type, a, b, c, d, xb,comments) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_INSERT_MODEL = "INSERT INTO model (id, descr) VALUES (?,'Загружено из Excel')";
    private static final String SQL_INSERT_MODEL_CALC = "INSERT INTO model_CALC (node,model_id, descr, parent_node, weight, level, is_leaf) VALUES (?,?,?,?,?,?,?)";
    private static final String SQL_INSERT_FORMULA_PARAMS = "INSERT INTO formula_params (node, param_code, year_shift) VALUES (?,?,?)";


    public DatabaseExcelImportAccessor(DataSource ds) throws SQLException {
        this.ds = ds;
        this.connection = ds.getConnection();
        connection.setAutoCommit(false);
        this.insertCompanyParamStmt = connection.prepareStatement(SQL_INSERT_COMPANY_PARAM);
        this.insertCompanyStmt = connection.prepareStatement(SQL_INSERT_COMPANY);
        this.insertFormulaStmt = connection.prepareStatement(SQL_INSERT_FORMULA);
        this.insertModelStmt = connection.prepareStatement(SQL_INSERT_MODEL);
        this.insertModelCalcStmt = connection.prepareStatement(SQL_INSERT_MODEL_CALC);
        this.insertFormulaParamStmt = connection.prepareStatement(SQL_INSERT_FORMULA_PARAMS);
    }

    @Override
    public void close() throws Exception {
        tryToCloseAndExecute(insertModelStmt);
        tryToCloseAndExecute(insertModelCalcStmt);
        tryToCloseAndExecute(insertCompanyParamStmt);
        tryToCloseAndExecute(insertCompanyStmt);
        tryToCloseAndExecute(insertFormulaStmt);
        tryToCloseAndExecute(insertFormulaParamStmt);
        tryToCloseConnection(connection, hasErrors);
    }

    public void insertFormulaParam(String id, String paramCode, Integer yearShift) throws SQLException {
        insertFormulaParamStmt.setString(1, id);
        insertFormulaParamStmt.setString(2, paramCode);
        insertFormulaParamStmt.setInt(3, yearShift);
        insertFormulaParamStmtCounter = addReqToStmtBatch(insertFormulaParamStmtCounter, insertFormulaParamStmt);
    }

    /**
     * Добавляем компанию для инсерта
     *
     * @param inn инн
     * @return присвоенный идентификатор
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
     *
     * @throws SQLException при косяке работы со стейтментами
     */
    public void insertModelCalc(
            String node,
            String model_id,
            String descr,
            String parent_node,
            Double weight,
            Integer level,
            Integer isLeaf
    ) throws SQLException {
        insertModelCalcStmt.setString(1, node);
        insertModelCalcStmt.setString(2, model_id);
        insertModelCalcStmt.setString(3, descr);
        setNullableString(insertModelCalcStmt, 4, parent_node);
        insertModelCalcStmt.setDouble(5, weight);
        insertModelCalcStmt.setInt(6, level);
        insertModelCalcStmt.setInt(7, isLeaf);
        insertModelCalcStmtCounter = addReqToStmtBatch(insertModelCalcStmtCounter, insertModelCalcStmt);
    }

    @SneakyThrows
    private void setNullableString(PreparedStatement stmtToSet, int position, String strValue) {
        if (strValue == null) {
            stmtToSet.setNull(position, Types.VARCHAR);
        } else {
            stmtToSet.setString(position, strValue);
        }

    }

    /**
     * Добавляем модель для инсерта
     *
     * @throws SQLException при косяке работы со стейтментами
     */
    public void insertModel(String id) throws SQLException {
        insertModelStmt.setString(1, id);
        insertModelStmtCounter = addReqToStmtBatch(insertModelStmtCounter, insertModelStmt);
    }

    /**
     * Добавляем компанию для инсерта
     *
     * @param inn инн
     * @return присвоенный идентификатор
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
     *
     * @param node        имя узла иерархии расчета
     * @param calculation способ подсчета входного значения
     * @param descr       описание формулы
     * @param formulaType тип формулы
     * @param a           параметр А
     * @param b           параметр B
     * @param c           параметр C
     * @param d           параметр D
     * @param xb          параметр XB
     * @throws SQLException если че-то не так с БД
     */
    public void insertFormula(
            String node,
            String descr,
            String calculation,
            String formulaType,
            String a,
            String b,
            String c,
            String d,
            String xb,
            String comments
    ) throws SQLException {
        int paramCounter = 1;
        insertFormulaStmt.setString(paramCounter++, node);
        insertFormulaStmt.setString(paramCounter++, calculation);
        insertFormulaStmt.setString(paramCounter++, descr);
        insertFormulaStmt.setString(paramCounter++, formulaType);
        insertFormulaStmt.setString(paramCounter++, a);
        insertFormulaStmt.setString(paramCounter++, b);
        insertFormulaStmt.setString(paramCounter++, c);
        insertFormulaStmt.setString(paramCounter++, d);
        insertFormulaStmt.setString(paramCounter++, xb);
        insertFormulaStmt.setString(paramCounter, comments);
        insertFormulaStmtCounter = addReqToStmtBatch(insertFormulaStmtCounter, insertFormulaStmt);
    }

    /**
     * Добавляем параметр компании для сохранения в базу.
     *
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
        insertCompanyParamStmtCounter = addReqToStmtBatch(insertCompanyParamStmtCounter, insertCompanyParamStmt);
    }


}