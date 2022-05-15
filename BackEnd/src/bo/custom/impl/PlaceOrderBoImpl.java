package bo.custom.impl;

import bo.custom.PlaceOrderBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dao.custom.QueryDAO;
import dto.CustomDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import entity.Order;
import entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceOrderBoImpl implements PlaceOrderBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    QueryDAO queryDAO=(QueryDAO)DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.QUERY);

    @Override
    public String createOrderId(Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.createOrderId(connection);
    }

    @Override
    public boolean placeOrder(OrderDTO dto, Connection con) throws SQLException, ClassNotFoundException {

        Connection connection = null;

        connection = con;
        connection.setAutoCommit(false);

        Order order = new Order(dto.getOrderId(), dto.getDate(), dto.getCustId(), dto.getCost(), dto.getDiscount());
        boolean addOrder = orderDAO.add(order, connection);
        if (!addOrder) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }


        for (OrderDetailDTO od : dto.getOrderDetail()) {
            OrderDetail detail = new OrderDetail(od.getOrderId(), od.getItemId(), od.getQty(), od.getPrice());
            boolean addOrderDetail = orderDetailDAO.add(detail, connection);

            if (!addOrderDetail) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            boolean updateItem = itemDAO.updateQty(od.getItemId(), od.getQty(), connection);
            if (!updateItem) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

        }
        connection.commit();
        connection.setAutoCommit(true);
        connection.close();
        return true;

    }

    @Override
    public boolean deleteOrder(Connection connection, String s) throws SQLException, ClassNotFoundException {
        return orderDAO.delete(connection, s);
    }

    @Override
    public ArrayList<Order> getOrder(String id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public String getOrderCount(Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.getOrderCount(connection);


    }

    @Override
    public ArrayList<CustomDTO> getDetailArray(Connection connection) throws SQLException, ClassNotFoundException {
        return queryDAO.getOrderDetailArrayWithCusName(connection);
    }

    @Override
    public CustomDTO getDetailObject(Connection connection, String id) throws SQLException, ClassNotFoundException {
        CustomDTO object = queryDAO.getOrderDetailObjectWithCusName(connection, id);
        if (object!=null){
            return object;
        }else {
            return null;

        }
    }
}
