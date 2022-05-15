package bo.custom.impl;

import bo.custom.ItemBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dto.ItemDTO;
import entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBoImpl implements ItemBO {
    ItemDAO itemDAO=(ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    @Override
    public boolean addItem(ItemDTO dto, Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.add(new Item(dto.getId(),dto.getName(),dto.getPrice(),dto.getQty()),connection);
    }

    @Override
    public boolean updateItem(Connection connection, ItemDTO dto) throws SQLException, ClassNotFoundException {
        return itemDAO.update(connection,new Item(dto.getId(),dto.getName(),dto.getPrice(),dto.getQty()));
    }

    @Override
    public boolean deleteItem(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(connection,id);
    }

    @Override
    public ItemDTO searchItem(Connection connection, String id) throws SQLException, ClassNotFoundException {
        Item search = itemDAO.search(connection, id);
        if (search!=null){
            return new ItemDTO(search.getItemId(),search.getItemName(),search.getUnitPrice(),search.getQty());
        }else {
            return null;
        }
    }

    @Override
    public ArrayList<ItemDTO> getAllItemDetails(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Item> items = itemDAO.getAll(connection);
        ArrayList<ItemDTO> itemDTOS=new ArrayList<>();
        for (Item i:items){
            itemDTOS.add(new ItemDTO(i.getItemId(),i.getItemName(),i.getUnitPrice(),i.getQty()));
        }
        return itemDTOS;
    }

    @Override
    public ArrayList<String> getAllItemIds(Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.getAllItemIds(connection);

    }

    @Override
    public String getItemCount(Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.getItemCount(connection);

    }
}
