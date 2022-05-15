package dao;

import dao.SuperDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO <T,ID,CON> extends SuperDAO {
    boolean add(T t,CON con) throws SQLException, ClassNotFoundException;

    boolean delete(CON con ,ID id) throws SQLException, ClassNotFoundException;

    boolean update(CON con,T t) throws SQLException, ClassNotFoundException;

    T search(CON con,ID id) throws SQLException, ClassNotFoundException;

    ArrayList<T> getAll(CON con) throws SQLException, ClassNotFoundException;
}
