package controller;

import dao.Customer;
import dao.CustomerDAOImpl;
import utils.ParseUtils;
import utils.ValidationUtils;
import view.MyFrame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class CustomerController {
    private final CustomerDAOImpl customerDAO;
    private final MyFrame panel;

    public CustomerController(MyFrame panel) {
        this.panel = panel;
        customerDAO = new CustomerDAOImpl();
    }

    public boolean addCustomer(String name, String phoneNumber, String email) {

        String parsedPhoneN = ParseUtils.parsePhoneNumber(phoneNumber, panel);
        if (parsedPhoneN == null) return false;

        String validationResult = ValidationUtils.validateName(name);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validatePhoneNumber(parsedPhoneN);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validateEmail(email);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }


        Customer customer = new Customer(name, parsedPhoneN, email);
        if (!customerDAO.insertCustomer(customer)) {
            panel.showErrorMessage("Inserting Customer failed!");
            return false;
        }
        return true;
    }


    public boolean updateCustomer(final String customerId, String name, String phoneNumber, String email) {

        Integer parsedID = ParseUtils.parseId(customerId, panel);
        if (parsedID == null) return false;
        String parsedPhoneN = ParseUtils.parsePhoneNumber(phoneNumber, panel);
        if (parsedPhoneN == null) return false;

        String validationResult = ValidationUtils.validateId(parsedID);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validateName(name);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validatePhoneNumber(parsedPhoneN);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validateEmail(email);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }


        Customer customer = new Customer(parsedID, name, parsedPhoneN, email);
        if (!customerDAO.updateCustomer(customer)) {
            panel.showErrorMessage("Updating Customer failed!");
            return false;
        }
        return true;
    }

    public boolean deleteCustomer(final String custormerId) {

        Integer custormerID = ParseUtils.parseId(custormerId, panel, false);
        if (custormerID == null) return false;

        String validationResult = ValidationUtils.validateId(custormerID);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }

        // TODO: check if Customer is in a order before deleting
        //  if (orderItemDAOImp.isProductInUse(productId)) {
        //      ("Cannot delete product as it is in use in one or more orders.");
        //  }
        //  instantiate a orderItemDAOImp on constructor

        // TODO: change delete to update the field is_deleted

        if (!customerDAO.deleteCustomer(custormerID)) {
            panel.showErrorMessage("Deleting Customer failed!");
            return false;
        }
        return true;
    }

    public ResultSet getCustomer(String customerId) {
        Integer id = ParseUtils.parseId(customerId, panel);
        ResultSet rs = customerDAO.listCustomer(id);
        if (rs == null) {
            panel.showErrorMessage("Listing Customer failed!");
        }
        return rs;
    }

    public ResultSet getAllCustomers() {
        ResultSet rs = customerDAO.listAllCustomers();
        if (rs == null) {
            panel.showErrorMessage("Listing all Customers failed!");
        }
        return rs;
    }

    public Vector<Customer> getAllCustomersVector() {
        Vector<Customer> customers = new Vector<>();
        ResultSet rs = customerDAO.listAllCustomers();
        while (true) {
            try {
                if (!rs.next()) break;
                customers.add(new Customer(rs.getInt("id"), rs.getString("name")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return customers;
    }
}

