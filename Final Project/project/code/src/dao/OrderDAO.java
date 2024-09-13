package dao;

import java.sql.ResultSet;

public interface OrderDAO {

    boolean insertOrder(int customerId);

    boolean updateOrder(int orderId, int customerId);

    boolean deleteOrder(int orderId);

    ResultSet listOrder(int orderId);

    ResultSet listAllOrders();
}
