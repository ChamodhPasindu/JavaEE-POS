package dao.custom;

import dao.CrudDAO;
import entity.Order;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Order,String, Connection> {
    public String getOrderCount(Connection connection) throws SQLException, ClassNotFoundException;
    String createOrderId(Connection connection) throws SQLException, ClassNotFoundException;

}
