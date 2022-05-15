package db;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource ds;

    private static DbConnection dbConnection=null;
    private Connection connection;

    private DbConnection() {
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DbConnection getInstance() {
        if (dbConnection==null){
            dbConnection= new DbConnection();
        }
        return dbConnection;

    }

    public Connection getConnection(){
        return connection;
    }
}
