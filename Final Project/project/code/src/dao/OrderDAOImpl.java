package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static utils.DbConnection.getConnection;

public class OrderDAOImpl implements OrderDAO {

    private PreparedStatement ps;
    private static final Connection con = getConnection();

    @Override
    public boolean insertOrder(int customerId) {

        String sql = "INSERT INTO orders (customer_id) values (?);";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setInt(1, customerId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to insert order", ex);
        }
    }

    @Override
    public boolean updateOrder(int orderId, int customerId) {

        String sql = "UPDATE orders SET customer_id = ? WHERE id = ?";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setInt(2, orderId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to update order", ex);
        }
    }

    @Override
    public boolean deleteOrder(int orderId) {

        String sql = "UPDATE orders SET is_deleted = true WHERE id = ?";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setInt(1, orderId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to delete order", ex);
        }
    }

    @Override
    public ResultSet listOrder(int orderId) {

        String sql = "SELECT id, customer_id, totalPrice FROM orders WHERE id = ?";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setInt(1, orderId);
            return ps.executeQuery();
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to list order", ex);
        }
    }

    @Override
    public ResultSet listAllOrders() {

        String sql = "SELECT orders.id as id, customer_id, customers.name as name, totalPrice FROM orders " +
                "join customers on (orders.customer_id = customers.id) " +
                "WHERE orders.is_deleted = false";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to list all orders", ex);
        }
    }
}
