package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.QueryDAO;
import dto.CustomDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public ArrayList<CustomDTO> getOrderDetailArrayWithCusName(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<CustomDTO> customDTOArrayList = new ArrayList<>();
        ResultSet rst = CrudUtil.executeQuery("select orderId,custName,date,discount,cost from Orders join Customer on Orders.custId=Customer.custId;", connection);

        while (rst.next()){
            customDTOArrayList.add(new CustomDTO(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getDouble(5)));
        }
        return customDTOArrayList;
    }

    @Override
    public CustomDTO getOrderDetailObjectWithCusName(Connection connection,String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select orderId,custName,date,discount,cost from Orders join Customer on Orders.custId=Customer.custId where orderId=?",
                connection, id);
        if (rst.next()){
            return new CustomDTO(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getDouble(5));

        }else {
            return null;
        }

    }
}
