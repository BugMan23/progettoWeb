package it.unical.progweb.persistence;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConn {
    private static DataSource dataSource;

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource non inizializzato");
        }
        return dataSource.getConnection();
    }

    public static void setDataSource(DataSource ds) {
        DbConn.dataSource = ds;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}