package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static utils.DbConnection.getConnection;

public class CustomerDAOImpl implements CustomerDAO {

    private PreparedStatement ps;
    private static final Connection con = getConnection();

    @Override
    public boolean insertCustomer(Customer customer) {

        String sql = "INSERT INTO customers (name, phoneNumber, email) values (?, ?, ?);";

        try (Connection con = getConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw new RuntimeException("Failed to insert customer", e);
        }
    }

    @Override
    public boolean updateCustomer(Customer customer) {

        String sql = "UPDATE customers SET name = ?, phoneNumber = ?, email = ? WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());
            ps.setInt(4, customer.getId());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw new RuntimeException("Failed to update customer", e);
        }
    }

    @Override
    public boolean deleteCustomer(int customerId) {

        String sql = "UPDATE customers SET is_deleted = true WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ps.setInt(1, customerId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw new RuntimeException("Failed to delete customer", e);
        }
    }

    @Override
    public ResultSet listCustomer(int customerId) {

        String sql = "SELECT id, name, phoneNumber, email FROM customers WHERE id = ? AND is_deleted = false";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setInt(1, customerId);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw new RuntimeException("Failed to list customer", e);
        }
    }

    @Override
    public ResultSet listAllCustomers() {

        String sql = "SELECT id, name, phoneNumber, email FROM customers WHERE is_deleted = false";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw new RuntimeException("Failed to list all customers", e);
        }
    }

    @Override
    public Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        if (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String phoneN = rs.getString("phoneNumber");
            String email = rs.getString("email");
            return new Customer(id, name, phoneN, email);
        }
        return null;
    }
}
