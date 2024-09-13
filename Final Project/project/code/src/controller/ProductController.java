package controller;


import dao.Product;
import dao.ProductDAOImpl;
import utils.ParseUtils;
import utils.ValidationUtils;
import view.MyFrame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ProductController {
    private final MyFrame panel;
    private final ProductDAOImpl productDAO;

    public ProductController(MyFrame panel) {
        this.panel = panel;
        productDAO = new ProductDAOImpl();
    }

    public boolean addProduct(String name, String price, String quantity) {

        Float parsedPrice = ParseUtils.parsePrice(price, panel);
        if (parsedPrice == null) return false;
        Integer parsedQuantity = ParseUtils.parseQuantity(quantity, panel);
        if (parsedQuantity == null) return false;


        String validationResult = ValidationUtils.validateName(name);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validatePrice(parsedPrice);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validateQuantity(parsedQuantity, 0);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }


        Product product = new Product(name, parsedPrice, parsedQuantity);
        if (!productDAO.insertProduct(product)) {
            panel.showErrorMessage("Inserting Product failed!");
            return false;
        }
        return true;
    }


    public boolean updateProduct(final String productId, String name, String price, String quantity) {

        Integer parsedID = ParseUtils.parseId(productId, panel);
        if (parsedID == null) return false;
        Float parsedPrice = ParseUtils.parsePrice(price, panel);
        if (parsedPrice == null) return false;
        Integer parsedQuantity = ParseUtils.parseQuantity(quantity, panel);
        if (parsedQuantity == null) return false;

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
        validationResult = ValidationUtils.validatePrice(parsedPrice);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validateQuantity(parsedQuantity, 0);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }


        Product product = new Product(parsedID, name, parsedPrice, parsedQuantity);
        if (!productDAO.updateProduct(product)) {
            panel.showErrorMessage("Updating Product failed!");
            return false;
        }
        return true;
    }

    public boolean deleteProduct(final String productId) {

        Integer productID = ParseUtils.parseId(productId, panel, false);
        if (productID == null) return false;

        String validationResult = ValidationUtils.validateId(productID);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }

        // TODO: check if Product is in a order item before deleting
        //  if (orderItemDAOImp.isProductInUse(productId)) {
        //      ("Cannot delete product as it is in use in one or more orders.");
        //  }
        //  instantiate a orderItemDAOImp on constructor

        // TODO: change delete to update the field is_deleted

        if (!productDAO.deleteProduct(productID)) {
            panel.showErrorMessage("Deleting Product failed!");
            return false;
        }
        return true;
    }

    public ResultSet getProduct(String productId) {
        Integer id = ParseUtils.parseId(productId, panel);
        ResultSet rs = productDAO.listProduct(id);
        if (rs == null) {
            panel.showErrorMessage("Listing Product failed!");
        }
        return rs;
    }

    public ResultSet getAllProducts() {
        ResultSet rs = productDAO.listAllProduct();
        if (rs == null) {
            panel.showErrorMessage("Listing all Products failed!");
        }
        return rs;
    }

    public Vector<Product> getAllProductsVector() {
        Vector<Product> products = new Vector<>();
        ResultSet rs = productDAO.listAllProduct();
        while (true) {
            try {
                if (!rs.next()) break;
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getFloat("price"), rs.getInt("quantity")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return products;
    }
}

