package it.unical.progweb.persistence;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConn{
    private static DataSource ds;
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if(connection == null){
            connection = ds.getConnection();
        }
        return connection;
    }

    public static void setDataSource(DataSource ds){
        DBConn.ds = ds;
    }

    public static DataSource getDataSource(){
        return ds;
    }

}