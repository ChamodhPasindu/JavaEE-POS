package dao.custom;

import dao.SuperDAO;
import dto.CustomDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    ArrayList<CustomDTO> getOrderDetailArrayWithCusName(Connection connection) throws SQLException, ClassNotFoundException;
    CustomDTO  getOrderDetailObjectWithCusName(Connection connection,String id) throws SQLException, ClassNotFoundException;
}
