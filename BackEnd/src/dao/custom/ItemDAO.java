package dao.custom;

import dao.CrudDAO;
import entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemDAO extends CrudDAO<Item,String, Connection> {
    ArrayList<String> getAllItemIds(Connection connection) throws SQLException, ClassNotFoundException;
    public String getItemCount(Connection connection) throws SQLException, ClassNotFoundException;
    boolean updateQty(String itemCode,int qty,Connection connection) throws SQLException, ClassNotFoundException;

}
