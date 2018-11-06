package ru.mifi.service.risk.database;

import lombok.SneakyThrows;
import ru.mifi.service.risk.domain.CalculationParamKey;
import ru.mifi.service.risk.domain.DataKey;
import ru.mifi.service.risk.domain.Formula;
import ru.mifi.service.risk.domain.FormulaResult;
import ru.mifi.service.risk.domain.HierarchyNode;
import ru.mifi.service.risk.domain.HierarchyResult;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
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
                    "   f.id, f.descr, f.calculation, f.formula_type, f.a, f.b, f.c, f.d, f.xb, f.comments, mc.weight " +
                    "FROM " +
                    "   model_calc mc " +
                    "JOIN formula f ON (f.id = mc.node)" +
                    "WHERE mc.model_id = ? AND mc.is_leaf = '1'";

    private static final String SQL_GET_COMPANY_IDS_BY_LIST_ID =
            "SELECT " +
                    "   company_ids" +
                    " FROM " +
                    "   company_list " +
                    "WHERE id = ?";
    private static final String SQL_SAVE_FORMULA_TO_TEMP_TABLE =
            "INSERT INTO " +
                    "   %s" +
                    "       (company_id, node, value, comment)" +
                    "   VALUES" +
                    "       (?,         ?,      ?,      ?)";
    private static final String SQL_SAVE_RESULT_TABLE_NAME =
            "INSERT INTO " +
                    "   result_data_mapper" +
                    "       (model_id, company_list_id, all_company_list_id, year, table_name)" +
                    "   VALUES" +
                    "       (?,?,?,?,?)";
    private static final String SQL_GET_NORMATIVE_VALUE_STMT =
            "SELECT " +
                    "   value, descr " +
                    "FROM " +
                    "   normative_parameters " +
                    "WHERE " +
                    "   param_name = ?";
    private static final String SQL_GET_FORMULA_PARAMS =
            "SELECT " +
                    "   cbp.company_id, cbp.param_code, cbp.year, cbp.param_value " +
                    "FROM " +
                    "   company_business_params cbp " +
                    "JOIN formula_params fp ON (fp.param_code = cbp.param_code) " +
                    "WHERE fp.node in (%s) AND cbp.company_id IN (%s) AND ((%s + fp.year_shift) = cbp.year)";

    private static final String SQL_MERGE_RESULT_DATA =
            "MERGE INTO";
    private static final String SQL_GET_MODEL_CALC =
            "SELECT " +
                    "  mc.descr, mc.node, mc.parent_node, mc.weight, mc.is_leaf, mc.level " +
                    "FROM model_calc mc " +
                    "WHERE mc.model_id = ?";
    private static final int SQL_BATCH_SIZE = 100;
    private static final String SQL_UPDATE_WEIGHT = "UPDATE %s t1 set t1.weight = (select weight from model_calc mc where mc.node=t1.node)";
    private static final String SQL_UPDATE_IS_LEAF = "UPDATE %s t1 set t1.is_leaf = (select is_leaf from model_calc mc where mc.node=t1.node)";
    private static final String SQL_UPDATE_PARENT_NODE = "UPDATE %s t1 set t1.parent_node = (select parent_node from model_calc mc where mc.node=t1.node)";
    private final PreparedStatement getModelCalcStmt;
    private final PreparedStatement getModelLeafsStmt;
    private final Statement getFuncParamsStmt;
    private final PreparedStatement getCompanyIdsByListId;
    private final PreparedStatement getNormativeValueStmt;
    private final PreparedStatement saveResultToMapperTableStmt;
    private DataSource dataSource;
    private Connection connection;

    public DatabaseCalculationAccessor(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        this.connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        this.getModelLeafsStmt = connection.prepareStatement(SQL_GET_MODEL_LEAFS);
        this.getFuncParamsStmt = connection.createStatement();
        this.getCompanyIdsByListId = connection.prepareStatement(SQL_GET_COMPANY_IDS_BY_LIST_ID);
        this.getModelCalcStmt = connection.prepareStatement(SQL_GET_MODEL_CALC);
        this.getNormativeValueStmt = connection.prepareStatement(SQL_GET_NORMATIVE_VALUE_STMT);
        this.saveResultToMapperTableStmt = connection.prepareStatement(SQL_SAVE_RESULT_TABLE_NAME);
    }

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
     * @param companyIds список идентификаторов компаний
     * @param year       Год расчета
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
                        "'" + String.join("','", formulaIds) + "'",
                        "'" + String.join("','", companyIds) + "'",
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
        tryToClose(getNormativeValueStmt);
        tryToClose(getFuncParamsStmt);
        tryToClose(getCompanyIdsByListId);
        tryToClose(getModelCalcStmt);
        tryToClose(saveResultToMapperTableStmt);
        tryToCloseConnection(connection, hasErrors);
    }

    public Map<HierarchyNode, String> getModelCalcHierarchy(String modelId) throws SQLException {
        Map<HierarchyNode, String> idsMap = new HashMap<>();
        getModelCalcStmt.setString(1, modelId);
        try (ResultSet resultSet = getModelCalcStmt.executeQuery()) {
            while (resultSet.next()) {
                idsMap.put(
                        new HierarchyNode(
                                resultSet.getString("node"),
                                resultSet.getDouble("weight")
                        ),
                        resultSet.getString("parent_node")
                );
            }
        }
        return idsMap;
    }

    @SneakyThrows
    public Double getNormativeValueByCode(String paramCode) {
        getNormativeValueStmt.setString(1, paramCode);
        try (ResultSet data = getNormativeValueStmt.executeQuery()) {
            if (!data.next()) {
                return null;
            }
            return data.getDouble("value");
        }
    }

    /**
     * Сохраняем результат расчета иерархии в базу
     *
     * @param finalResult результат расчета иерархии
     * @param key         объект-ключ для результатов расчета
     */
    @SneakyThrows
    public String saveDataToDb(
            HierarchyResult finalResult,
            CalculationParamKey key
    ) {
        String tableName = new DatabaseDdlAccessor().createTempTable(
                key,
                connection
        );
        Map<String, Collection<FormulaResult>> hierarchyData = finalResult.getHierarchyData();
        try (PreparedStatement saveFormulaStmt = connection.prepareStatement(
                String.format(
                        SQL_SAVE_FORMULA_TO_TEMP_TABLE,
                        tableName
                )
        )) {
            for (String inn : hierarchyData.keySet()) {
                int counter = 0;
                for (FormulaResult node : hierarchyData.get(inn)) {
                    saveFormulaStmt.setString(1, inn);
                    saveFormulaStmt.setString(2,
                            node.getId() == null
                                    ? "root"
                                    : node.getId()
                    );
                    saveFormulaStmt.setDouble(3, node.getResult());
                    saveFormulaStmt.setString(4, node.getComment());
                    saveFormulaStmt.addBatch();
                    if (++counter == SQL_BATCH_SIZE) {
                        saveFormulaStmt.executeBatch();
                    }
                    saveFormulaStmt.clearParameters();
                }
            }
            saveFormulaStmt.executeBatch();
        }
        mergeData(tableName);
        //Сохраняем данные в таблицу-маппинг результатов
        saveResultToMapperTableStmt.setString(1, key.getModelId());
        saveResultToMapperTableStmt.setString(2, key.getCompanyListId());
        saveResultToMapperTableStmt.setString(3, key.getAllCompaniesListId());
        saveResultToMapperTableStmt.setInt(4, key.getYear());
        saveResultToMapperTableStmt.setString(5, tableName);
        saveResultToMapperTableStmt.executeUpdate();
        saveResultToMapperTableStmt.clearParameters();
        return tableName;
    }

    @SneakyThrows
    private void mergeData(String tableName) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(String.format(SQL_UPDATE_WEIGHT, tableName));
            stmt.execute(String.format(SQL_UPDATE_IS_LEAF, tableName));
            stmt.execute(String.format(SQL_UPDATE_PARENT_NODE, tableName));
        }
    }
}
