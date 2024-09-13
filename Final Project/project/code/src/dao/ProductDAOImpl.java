package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static utils.DbConnection.getConnection;

public class ProductDAOImpl implements ProductDAO {

    private PreparedStatement ps;
    private static final Connection con = getConnection();

    @Override
    public boolean insertProduct(Product product) {

        String sql = "INSERT INTO products (name, price, quantity) values (?, ?, ?);";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setFloat(2, product.getPrice());
            ps.setFloat(3, product.getQuantity());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to insert product", ex);
        }
    }

    @Override
    public boolean updateProduct(Product product) {

        String sql = "UPDATE products SET name = ?, price = ?, quantity = ? WHERE id = ?";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setFloat(2, product.getPrice());
            ps.setFloat(3, product.getQuantity());
            ps.setInt(4, product.getId());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to update product", ex);
        }
    }

    @Override
    public boolean deleteProduct(int productId) {

        String sql = "UPDATE products SET is_deleted = true WHERE id = ?";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setInt(1, productId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to delete product", ex);
        }
    }

    @Override
    public ResultSet listProduct(int productId) {

        String sql = "SELECT id, name, price, quantity FROM products WHERE id = ? AND is_deleted = false";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            ps.setInt(1, productId);
            return ps.executeQuery();
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to list product", ex);
        }
    }

    @Override
    public ResultSet listAllProduct() {

        String sql = "SELECT id, name, price, quantity FROM products WHERE is_deleted = false";

        try {
            ps = Objects.requireNonNull(con).prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException ex) {
            System.err.println("SQL Exception: " + ex.getMessage());
            throw new RuntimeException("Failed to list all products", ex);
        }
    }

    @Override
    public Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        if (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            float price = rs.getFloat("price");
            int quantity = rs.getInt("quantity");
            return new Product(id, name, price, quantity);
        }
        return null;
    }
}
