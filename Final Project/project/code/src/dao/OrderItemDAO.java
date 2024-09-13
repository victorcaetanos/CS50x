package dao;

import exceptions.InsufficientQuantityException;
import exceptions.NegativeQuantityException;

import java.sql.ResultSet;

public interface OrderItemDAO {

    boolean insertOrderItem(OrderItem orderItem) throws NegativeQuantityException, InsufficientQuantityException;

    boolean deleteOrderItem(int orderId, int productId, int quantity) throws InsufficientQuantityException, NegativeQuantityException;

    ResultSet listOrderItem(int orderId, int productId);

    ResultSet listAllOrderItems(int orderId);
}
