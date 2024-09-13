package dao;

import exceptions.InsufficientQuantityException;
import exceptions.NegativeQuantityException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static utils.DbConnection.getConnection;

public class OrderItemDAOImpl implements OrderItemDAO {

    private final Connection con = getConnection();
    private PreparedStatement ps;

    @Override
    public boolean insertOrderItem(OrderItem orderItem) throws NegativeQuantityException, InsufficientQuantityException {

        String sql_select = "SELECT * FROM orderitems WHERE order_id = ? AND product_id = ?;";
        String sql_update = "UPDATE orderitems SET quantity = quantity + ? WHERE order_id = ? AND product_id = ?;";
        String sql_insert = "INSERT INTO orderitems (order_id, product_id, quantity, unitPrice) values (?, ?, ?, ?);";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql_select);
            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getProductId());

            // if Order already has OrderItem, update quantity
            if (ps.executeQuery().next()) {
                ps = con.prepareStatement(sql_update);
                ps.setInt(1, orderItem.getQuantity());
                ps.setInt(2, orderItem.getOrderId());
                ps.setInt(3, orderItem.getProductId());
            } else {
                ps = con.prepareStatement(sql_insert);
                ps.setInt(1, orderItem.getOrderId());
                ps.setInt(2, orderItem.getProductId());
                ps.setInt(3, orderItem.getQuantity());
                ps.setFloat(4, orderItem.getUnitPrice());
            }
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            if (ex.getSQLState().equals("45001")) {
                throw new InsufficientQuantityException();
            } else if (ex.getSQLState().equals("45002")) {
                throw new NegativeQuantityException();
            } else {
                throw new RuntimeException("Failed to insert order items", ex);
            }
        }
    }

    @Override
    public boolean deleteOrderItem(int orderId, int productId, int quantity) throws InsufficientQuantityException, NegativeQuantityException {

        String sql = "UPDATE orderitems SET quantity = quantity - ? " +
                "WHERE order_id = ? AND product_id = ?";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setInt(2, orderId);
            ps.setInt(3, productId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            if (ex.getSQLState().equals("45001")) {
                throw new InsufficientQuantityException();
            } else if (ex.getSQLState().equals("45002")) {
                throw new NegativeQuantityException();
            } else {
                throw new RuntimeException("Failed to delete order", ex);
            }
        }
    }

    @Override
    public ResultSet listOrderItem(int orderId, int productId) {

        String sql = "SELECT product_id, quantity, unitPrice, totalPrice FROM orderitems WHERE order_id = ? AND product_id = ?";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            return ps.executeQuery();
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to list order", ex);
        }
    }

    @Override
    public ResultSet listAllOrderItems(int orderId) {

        String sql = "SELECT product_id, products.name AS productName, orderitems.quantity, unitPrice, totalPrice FROM orderitems " +
                "JOIN products on (orderitems.product_id = products.id) " +
                "WHERE order_id = ? and orderitems.quantity > 0";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setInt(1, orderId);
            return ps.executeQuery();
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to list all order items", ex);
        }
    }
}
