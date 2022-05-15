package bo.custom;

import bo.SuperBO;
import dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO extends SuperBO {
    boolean addCustomer(CustomerDTO dto, Connection connection) throws SQLException, ClassNotFoundException;
    boolean updateCustomer(Connection connection,CustomerDTO dto) throws SQLException, ClassNotFoundException;
    boolean deleteCustomer(Connection connection,String id) throws SQLException, ClassNotFoundException;
    CustomerDTO searchCustomer(Connection connection,String id) throws SQLException, ClassNotFoundException;
    ArrayList<CustomerDTO>getAllCustomerDetails(Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<String>getAllCustomerIds(Connection connection) throws SQLException, ClassNotFoundException;
    public String getCustomerCount(Connection connection) throws SQLException, ClassNotFoundException;
}
