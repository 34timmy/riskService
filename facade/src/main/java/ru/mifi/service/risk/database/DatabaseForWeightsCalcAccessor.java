package ru.mifi.service.risk.database;

import org.springframework.stereotype.Component;
import ru.mifi.service.risk.dto.ModelCalcDto;
import ru.mifi.service.risk.dto.ModelDto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.mifi.service.risk.dto.ModelCalcDto.initIndexCode;

/**
 * Содержит запросы к БД, необходимые для расчета внешней утилитой весов для иерархии модели.
 *
 * @author DenRUS
 */
@Component
public class DatabaseForWeightsCalcAccessor extends CustomAutoCloseable{

    public static final String SQL_GET_ALL_MODELS =
            "SELECT id, name, descr FROM model";
    public static final String SQL_GET_MODEL_CALC_BY_MODEL_ID =
            "select node, parent_id, descr, expert_value, weight, level FROM model_calc WHERE model_id = ?";
    public static final String SQL_UPDATE_WEIGHTS_MODEL_CALC =
            "UPDATE model_calc SET weight=?, expert_value=? WHERE node=?";
    private Connection connection;

    private PreparedStatement getAllModelsStmt;
    private PreparedStatement getModelCalcByModelIdStmt;
    private PreparedStatement updateWeightsOfModelCalcStmt;
    private Integer updateBatchCounter = 0;

    private DataSource dataSource;
    private boolean inited = false;

    /**
     * Стандартный конструктор. Получаем датасорс и подготавливаем все необходимые stmt.
     * @param dataSource датасорс к БД
     * @throws SQLException в случае ошибки подготовки стейтментов.
     */
    public DatabaseForWeightsCalcAccessor(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
    }

    /**
     * Сразу подготавливать конекшены не выходит - миграции выполняются позже, чем бин создается - таблиц ещё нет.
     * @throws SQLException при косяке подготовки стейтментов
     */
    public DatabaseForWeightsCalcAccessor init() throws SQLException {
        if (inited) {
            return this;
        }
        this.connection = dataSource.getConnection();
        this.getAllModelsStmt = connection.prepareStatement(SQL_GET_ALL_MODELS);
        this.getModelCalcByModelIdStmt = connection.prepareStatement(SQL_GET_MODEL_CALC_BY_MODEL_ID);
        this.updateWeightsOfModelCalcStmt = connection.prepareStatement(SQL_UPDATE_WEIGHTS_MODEL_CALC);
        this.inited = true;
        return this;
    }

    public void updateWeightsOfModelCalc(Collection<ModelCalcDto> forUpdate) throws SQLException {
        for (ModelCalcDto modelCalcDto : forUpdate) {
            updateWeightsOfModelCalcStmt.setDouble(1, modelCalcDto.getWeight());
            updateWeightsOfModelCalcStmt.setDouble(2, modelCalcDto.getExpertValue());
            updateWeightsOfModelCalcStmt.setString(3, modelCalcDto.getNodeId());
            addReqToStmtBatch(updateBatchCounter, updateWeightsOfModelCalcStmt);
        }
        updateWeightsOfModelCalcStmt.executeBatch();
    }
    /**
     * По идентификатору модели получаем все ноды её иерархии в ДТО.
     * @param modelId идентификатор моделей
     * @return коллекция ДТО нод иерархии модели
     * @throws SQLException при ошибке работы с БД
     */
    public Collection<ModelCalcDto> getAllNodesByModelId(String modelId) throws SQLException {
        getModelCalcByModelIdStmt.setString(1, modelId);
        ResultSet rs = getModelCalcByModelIdStmt.executeQuery();
        List<ModelCalcDto> resultList = new ArrayList<>();
        while (rs.next()) {
            resultList.add(
                    new ModelCalcDto(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getDouble(4),
                            rs.getDouble(5),
                            rs.getInt(6)
                    )
            );
        }
        return initIndexCode(resultList);
    }
    /**
     * Получаем все имеющиеся модели с их описанием.
     * @return коллекция DTO для моделей
     * @throws SQLException при ошибке работы с базой
     */
    public Collection<ModelDto> getAllModels() throws SQLException {
        List<ModelDto> resultList = new ArrayList<>();
        ResultSet rs = getAllModelsStmt.executeQuery();
        while (rs.next()) {
            resultList.add(
                    new ModelDto(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3)
                    )
            );
        }
        return resultList;
    }


    @Override
    public void close() throws Exception {
        tryToClose(getModelCalcByModelIdStmt);
        tryToClose(getAllModelsStmt);
        tryToCloseAndExecute(updateWeightsOfModelCalcStmt);
        tryToCloseConnection(connection, false);
    }
}
