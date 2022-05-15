package bo.custom.impl;

import bo.custom.CustomerBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dto.CustomerDTO;
import entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBoImpl implements CustomerBO {
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public boolean addCustomer(CustomerDTO dto, Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.add(new Customer(dto.getId(),dto.getName(),dto.getSalary(),dto.getAddress()),connection);
    }

    @Override
    public CustomerDTO searchCustomer(Connection connection,String id) throws SQLException, ClassNotFoundException {

        Customer search = customerDAO.search(connection,id);
        if (search!=null){
            return new CustomerDTO(search.getCustId(),search.getCustName(),search.getAddress(),search.getCustSalary());
        }else {
            return null;
        }
    }

    @Override
    public ArrayList<String> getAllCustomerIds(Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.getAllCustomerIds(connection);
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomerDetails(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Customer> customers = customerDAO.getAll(connection);
        ArrayList<CustomerDTO> customerDTOS=new ArrayList<>();
        for (Customer c:customers){
            customerDTOS.add(new CustomerDTO(c.getCustId(),c.getCustName(),c.getAddress(),c.getCustSalary()));
        }
        return customerDTOS;
    }

    @Override
    public String getCustomerCount(Connection connection) throws SQLException, ClassNotFoundException {

        return customerDAO.getCustomerCount(connection);
    }

    @Override
    public boolean updateCustomer(Connection connection,CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(connection,new Customer(dto.getId(),dto.getName(),dto.getSalary(),dto.getAddress()));
    }

    @Override
    public boolean deleteCustomer(Connection connection,String id) throws SQLException, ClassNotFoundException {

        return customerDAO.delete(connection,id);
    }
}
