package dao;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface ProductDAO {

    boolean insertProduct(Product product);

    boolean updateProduct(Product product);

    boolean deleteProduct(int productId);

    ResultSet listProduct(int productId);

    ResultSet listAllProduct();

    Product mapResultSetToProduct(ResultSet rs) throws SQLException;
}
