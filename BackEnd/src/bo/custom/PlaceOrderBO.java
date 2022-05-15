package bo.custom;

import bo.SuperBO;
import dto.CustomDTO;
import dto.OrderDTO;
import entity.Order;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface PlaceOrderBO extends SuperBO {
    String createOrderId(Connection connection) throws SQLException, ClassNotFoundException;

    boolean placeOrder(OrderDTO dto,Connection connection) throws SQLException, ClassNotFoundException;

    boolean deleteOrder(Connection connection,String s) throws SQLException, ClassNotFoundException;

    ArrayList<Order> getOrder(String id) throws SQLException, ClassNotFoundException;
    public String getOrderCount(Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<CustomDTO>getDetailArray(Connection connection) throws SQLException, ClassNotFoundException;

    CustomDTO getDetailObject(Connection connection,String id) throws SQLException, ClassNotFoundException;
}
