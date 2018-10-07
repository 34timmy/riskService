package ru.mifi.service.risk.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Имплементация AutoCloseable с парой нужных методов
 * Created by DenRUS on 08.10.2018.
 */
abstract class CustomAutoCloseable implements AutoCloseable{

    private static final Logger LOG = LoggerFactory.getLogger(CustomAutoCloseable.class);

    boolean hasErrors = false;

    Integer addReqToStmtBatch(Integer counter, PreparedStatement stmt) throws SQLException {
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

    void tryToCloseConnection(Connection toClose, boolean hasErrors) throws SQLException {
        if (hasErrors) {
            toClose.rollback();
        } else {
            toClose.commit();
        }
        tryToClose(toClose);
    }
    void tryToClose(AutoCloseable toClose) {
        try {
            toClose.close();
        } catch (Exception ex) {
            LOG.error("Не удалось закрыть: " + toClose);
        }
    }
    void tryToCloseAndExecute(Statement stmt) throws SQLException {
        try {
            stmt.executeBatch();
        } catch (Exception ex) {
            LOG.error("Ошибка при выполнении батча: " + ex.getMessage(), ex);
            hasErrors = true;
        } finally {
            tryToClose(stmt);
        }
    }
}
