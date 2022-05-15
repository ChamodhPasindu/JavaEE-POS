package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.ItemDAO;
import entity.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean add(Item item, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("INSERT INTO Item VALUES (?,?,?,?)",connection,item.getItemId(),item.getItemName(),
                item.getUnitPrice(),item.getQty());
    }

    @Override
    public boolean delete(Connection connection,String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("delete from Item where itemId=?",connection,s);
    }

    @Override
    public boolean update(Connection connection,Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("Update Item set itemName=?,unitPrice=?,qty=? where itemId=?",connection,item.getItemName(),
                item.getUnitPrice(),item.getQty(),item.getItemId());
    }

    @Override
    public Item search(Connection connection,String s) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select * from Item where ItemId=?", connection, s);
        if (rst.next()){
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getInt(4)
            );
        }else {
            return null;
        }
    }

    @Override
    public ArrayList<Item> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select * from Item", connection);
        ArrayList<Item> items=new ArrayList<>();
        while (rst.next()){
            items.add(new Item(rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getInt(4)));
        }
        return items;
    }

    @Override
    public ArrayList<String> getAllItemIds(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select itemId from Item", connection);
        ArrayList<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(rst.getString(1));
        }
        return ids;
    }

    @Override
    public String getItemCount(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("select count(ItemId) from Item", connection);
        String count="0";
        while (rst.next()){
            count=rst.getString(1);
        }
        return count;
    }

    @Override
    public boolean updateQty(String itemId, int qty,Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("UPDATE Item SET qty=(qty-" + qty + ") WHERE itemId='" + itemId + "'",connection);
    }
}
