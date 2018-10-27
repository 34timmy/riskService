package ru.mifi.service.risk.database;

import ru.mifi.service.risk.domain.CompanyParam;
import ru.mifi.service.risk.domain.DataKey;
import ru.mifi.service.risk.domain.Formula;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Работа с базой при непосредственном расчете.
 * Created by DenRUS on 08.10.2018.
 */
public class DatabaseCalculationAccessor extends CustomAutoCloseable {

    //TODO надо сортировать формулы и потом доставать по 1000 для расчета. Чтоб не все сразу
    private static final String SQL_GET_MODEL_LEAFS =
            "SELECT " +
                    "   f.node, f.descr, f.calculation, f.formula_type, f.a, f.b, f.c, f.d, f.xb, f.comments, mc.weight " +
                    "FROM " +
                    "   model_calc mc " +
                    "JOIN formula f ON (f.node = mc.node)" +
                    "WHERE mc.model_id = ? AND mc.is_leaf = '1'";

    private static final String SQL_GET_COMPANY_IDS_BY_LIST_ID =
            "SELECT " +
                    "   company_ids" +
                    " FROM " +
                    "   company_list " +
                    "WHERE id = ?";
    private static final String SQL_GET_FORMULA_PARAMS =
            "SELECT " +
                    "   cbp.company_id, cbp.param_code, cbp.year, cbp.param_value " +
                    "FROM " +
                    "   company_business_params cbp " +
                    "JOIN formula_params fp ON (fp.param_code = cbp.param_code) " +
                    "WHERE fp.node in (%s) AND cbp.company_id IN (%s) AND ((%s + fp.year_shift) = cbp.year)";

    private final PreparedStatement getModelLeafsStmt;
    private final Statement getFuncParamsStmt;
    private final PreparedStatement getCompanyIdsByListId;

    public DatabaseCalculationAccessor(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        this.connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        this.getModelLeafsStmt = connection.prepareStatement(SQL_GET_MODEL_LEAFS);
        this.getFuncParamsStmt = connection.createStatement();
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
     * @param formulaIds идентификаторы формул
     * @param companyIds  список идентификаторов компаний
     * @param year  Год расчета
     * @return набор параметров
     */
    public Map<DataKey, Map<String, Double>> getParamsForFormula(
            Set<String> formulaIds,
            Set<String> companyIds,
            Integer year
    ) throws SQLException {
        Map<DataKey, Map<String, Double>> result = new HashMap<>();
        try (ResultSet params = getFuncParamsStmt.executeQuery(
                String.format(
                        SQL_GET_FORMULA_PARAMS,
                        "'" + formulaIds.stream().collect(Collectors.joining("','")) + "'",
                        "'" + companyIds.stream().collect(Collectors.joining("','")) + "'",
                        year
                )
        )) {
            while (params.next()) {
                Map<String, Double> data = new HashMap<>();
                data.put(params.getString("param_code"), params.getDouble("param_value"));
                result.merge(
                        new DataKey(
                                params.getInt("year"),
                                params.getString("company_id")
                        ),
                        data,
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        }
                );
            }
        }
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
