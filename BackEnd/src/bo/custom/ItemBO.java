package bo.custom;

import bo.SuperBO;
import dao.custom.ItemDAO;
import dto.CustomerDTO;
import dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemBO extends SuperBO {
    boolean addItem(ItemDTO dto, Connection connection) throws SQLException, ClassNotFoundException;
    boolean updateItem(Connection connection,ItemDTO dto) throws SQLException, ClassNotFoundException;
    boolean deleteItem(Connection connection,String id) throws SQLException, ClassNotFoundException;
    ItemDTO searchItem(Connection connection,String id) throws SQLException, ClassNotFoundException;
    ArrayList<ItemDTO> getAllItemDetails(Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<String>getAllItemIds(Connection connection) throws SQLException, ClassNotFoundException;
    public String getItemCount(Connection connection) throws SQLException, ClassNotFoundException;
}
