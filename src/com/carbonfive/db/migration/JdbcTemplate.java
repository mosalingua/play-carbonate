package com.carbonfive.db.migration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author huljas
 */
public class JdbcTemplate {
    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Object execute(ConnectionCallback connectionCallback) throws MigrationException {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            Object result = connectionCallback.doInConnection(connection);
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            return result;
        } catch (Exception e) {
            MigrationException migrationException = new MigrationException(e);
            try {
                if (connection != null && !connection.getAutoCommit()) {
                    connection.rollback();
                }
            } catch (SQLException sqle) {
                migrationException.addSuppressed(sqle);
            }
            throw migrationException;
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                throw new MigrationException(e);
            }
        }
    }
}
