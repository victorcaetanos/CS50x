package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CustomerDAO {

    boolean insertCustomer(Customer customer);

    boolean updateCustomer(Customer customer);

    boolean deleteCustomer(int customerId);

    ResultSet listCustomer(int customerId);

    ResultSet listAllCustomers();

    Customer mapResultSetToCustomer(ResultSet rs) throws SQLException;
}
