package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.CustomerDAO;
import db.DbConnection;
import entity.Customer;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean add(Customer c,Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO Customer VALUES (?,?,?,?)",connection,c.getCustId(),c.getCustName(),c.getCustSalary()
        ,c.getAddress());
    }

    @Override
    public boolean delete(Connection connection,String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("delete from Customer where custId=?",connection,s);
    }

    @Override
    public boolean update(Connection connection,Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("Update Customer set custName=?,custAddress=?,custSalary=? where custId=?",connection,
                customer.getCustName(),customer.getAddress(),customer.getCustSalary(),customer.getCustId());
    }

    @Override
    public Customer search(Connection connection,String s) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select * from Customer where custId=?", connection, s);
        if (rst.next()){
            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getString(4)
            );
        }else {
            return null;
        }
    }

    @Override
    public ArrayList<Customer> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select * from Customer", connection);
        ArrayList<Customer> customers=new ArrayList<>();
        while (rst.next()){
            customers.add(new Customer(rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getString(4)));
        }
        return customers;
    }

    @Override
    public ArrayList<String> getAllCustomerIds(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select custId from Customer", connection);
        ArrayList<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(rst.getString(1));
        }
        return ids;
    }

    @Override
    public String getCustomerCount(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select count(custID) from Customer", connection);
        String count="0";
        while (rst.next()){
            count=rst.getString(1);
        }
        return count;
    }
}
