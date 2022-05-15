package dao;

import db.DbConnection;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {



    private static PreparedStatement getPreparedStatement(String sql,Connection connection, Object... args) throws SQLException, ClassNotFoundException {

        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            pstm.setObject(i + 1, args[i]);
        }
        return pstm;
    }

    public static boolean  executeUpdate(String sql,Connection connection, Object... args) throws SQLException, ClassNotFoundException {
        return getPreparedStatement(sql, connection,args).executeUpdate() > 0;

    }


    public static ResultSet executeQuery(String sql, Connection connection, Object... args) throws SQLException, ClassNotFoundException {
        return getPreparedStatement(sql,connection, args).executeQuery();

    }

}
