package ru.mifi.service.risk.database;

import ru.mifi.service.risk.domain.CompanyParam;
import ru.mifi.service.risk.domain.Formula;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Работа с базой при непосредственном расчете.
 * Created by DenRUS on 08.10.2018.
 */
public class DatabaseCalculationAccessor extends CustomAutoCloseable {

    //TODO надо сортировать формулы и потом доставать по 1000 для расчета. Чтоб не все сразу
    private static final String SQL_GET_MODEL_LEAFS =
            "SELECT " +
                    "   f.node, f.descr, f.calculation, f.formula_type, f.a, f.b, f.c, f.d, f.xb, f.comments " +
                    "FROM " +
                    "   model_calc mc " +
                    "JOIN formula f ON (f.node = mc.node)" +
                    "WHERE mc.model_id = ? AND mc.is_leaf = '1'";

    private static final String SQL_GET_COMPANY_IDS_BY_LIST_ID =
            "SELECT " +
                    "   company_ids" +
                    "FROM " +
                    "   company_list " +
                    "WHERE id = ?";
    private static final String SQL_GET_FORMULA_PARAMS =
            "SELECT " +
                    "   cbp.company_id, cbp.param_code, cbp.year, cbp.param_value " +
                    "FROM " +
                    "   company_business_params cbp " +
                    "JOIN formula_params fp ON (fp.param_code = cbp.param_code) " +
                    "WHERE fp.node = ? AND cbp.company_id IN (?)";

    private final PreparedStatement getModelLeafsStmt;
    private final PreparedStatement getFuncParamsStmt;
    private final PreparedStatement getCompanyIdsByListId;

    public DatabaseCalculationAccessor(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        this.connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        this.getModelLeafsStmt = connection.prepareStatement(SQL_GET_MODEL_LEAFS);
        this.getFuncParamsStmt = connection.prepareStatement(SQL_GET_FORMULA_PARAMS);
        this.getCompanyIdsByListId = connection.prepareStatement(SQL_GET_COMPANY_IDS_BY_LIST_ID);
    }

    private DataSource dataSource;
    private Connection connection;


    /**
     * На основании идентификатора списка компаний получаем набор идентификаторов самих компаний.
     *
     * @param companyListId идентификатор списка компаний
     * @return идентификаторы компаний
     */
    public Set<String> getCompanyIdsByListId(String companyListId) throws SQLException {
        getCompanyIdsByListId.setObject(1, companyListId);
        ResultSet rs = getCompanyIdsByListId.executeQuery();
        if (rs.next()) {
            return Arrays.stream(rs.getString("company_ids").split(";"))
                    .collect(Collectors.toSet());
        }
        throw new IllegalArgumentException("Результат не найден.");
    }

    /**
     * Достаем все формулы-листья, которые нужно рассчитать для этой модели
     *
     * @param modelId идентификатор модели
     * @return набор формул
     * @throws SQLException если косяк при работе с БД
     */
    public Set<Formula> getFormulasForCalc(String modelId) throws SQLException {
        Set<Formula> result = new LinkedHashSet<>();
        getModelLeafsStmt.setString(1, modelId);
        try (ResultSet leafs = getModelLeafsStmt.executeQuery()) {
            while (leafs.next()) {
                result.add(new Formula(leafs));
            }
        }
        getModelLeafsStmt.clearParameters();
        return result;
    }

    /**
     * Получаем параметры по всем компаниям, необходимые для расчета конкретной формулы
     *
     * @param formulaNode идентификатор формулы (узла)
     * @param companyIds  список идентификаторов компаний
     * @return набор параметров
     */
    public Set<CompanyParam> getParamsForFormula(String formulaNode, Set<String> companyIds) throws SQLException {
        Set<CompanyParam> result = new HashSet<>();
        getFuncParamsStmt.setString(1, formulaNode);
        getFuncParamsStmt.setObject(1, companyIds);
        try (ResultSet params = getFuncParamsStmt.executeQuery()) {
            result.add(new CompanyParam(params));
        }
        getFuncParamsStmt.clearParameters();
        return result;
    }

    @Override
    public void close() throws Exception {
        tryToClose(getModelLeafsStmt);
        tryToClose(getFuncParamsStmt);
        tryToClose(getCompanyIdsByListId);
        tryToCloseConnection(connection, hasErrors);
    }
}
