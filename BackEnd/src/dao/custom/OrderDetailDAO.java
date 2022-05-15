package dao.custom;

import dao.CrudDAO;
import entity.OrderDetail;

import java.sql.Connection;

public interface OrderDetailDAO extends CrudDAO<OrderDetail,String, Connection> {
}
