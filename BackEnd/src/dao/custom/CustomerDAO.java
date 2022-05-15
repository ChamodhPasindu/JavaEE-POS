package dao.custom;

import dao.CrudDAO;
import entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDAO extends CrudDAO<Customer,String, Connection> {
    ArrayList<String> getAllCustomerIds(Connection connection) throws SQLException, ClassNotFoundException;
    public String getCustomerCount(Connection connection) throws SQLException, ClassNotFoundException;
}
