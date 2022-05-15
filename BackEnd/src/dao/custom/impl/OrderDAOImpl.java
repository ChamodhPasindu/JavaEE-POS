package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.OrderDAO;
import entity.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public boolean add(Order order, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO Orders VALUES (?,?,?,?,?)",connection,order.getOrderId(),order.getDate(),
                order.getCustId(),order.getCost(),order.getDiscount());
    }

    @Override
    public boolean delete(Connection connection,String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("delete from Orders where orderId=?",connection,s);

    }

    @Override
    public boolean update(Connection connection,Order order) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Order search(Connection connection,String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<Order> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public String getOrderCount(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select count(orderId) from Orders", connection);
        String count="0";
        while (rst.next()){
            count=rst.getString(1);
        }
        return count;
    }

    @Override
    public String createOrderId(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT OrderId FROM Orders ORDER BY OrderId DESC LIMIT 1", connection);
        if (rst.next()) {
            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                return "OID-00" + tempId;

            } else if (tempId <= 99) {
                return "OID-0" + tempId;

            } else {
                return "OID-" + tempId;
            }

        } else {
            return  "OID-001";

        }
    }
}
