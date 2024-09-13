package controller;

import dao.*;
import exceptions.InsufficientQuantityException;
import exceptions.NegativeQuantityException;
import utils.ParseUtils;
import utils.ValidationUtils;
import view.MyFrame;

import java.sql.ResultSet;

public class OrderItemController {
    private final OrderItemDAOImpl orderItemDAO;
    private final MyFrame panel;

    public OrderItemController(MyFrame panel) {
        this.panel = panel;
        orderItemDAO = new OrderItemDAOImpl();
    }

    public boolean addOrderItem(String orderId, Integer productId, String quantity, Float unitPrice) {

        Integer parsedOrderId = ParseUtils.parseId(orderId, panel);
        if (parsedOrderId == null) return false;
        Integer parsedQuantity = ParseUtils.parseQuantity(quantity, panel);
        if (parsedQuantity == null) return false;

        String validationResult = ValidationUtils.validateId(parsedOrderId);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validateId(productId);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validateQuantity(parsedQuantity);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validatePrice(unitPrice);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }

        OrderItem orderItem = new OrderItem(parsedOrderId, productId, parsedQuantity, unitPrice);
        try {
            if (!orderItemDAO.insertOrderItem(orderItem)) {
                panel.showErrorMessage("Inserting Order Item failed!");
                return false;
            }
        } catch (InsufficientQuantityException | NegativeQuantityException e) {
            panel.showErrorMessage(e.getMessage());
        }
        return true;
    }

    public boolean deleteOrderItem(String orderId, int productId, String quantity) {

        Integer parsedOrderId = ParseUtils.parseId(orderId, panel);
        if (parsedOrderId == null) return false;
        Integer parsedQuantity = ParseUtils.parseQuantity(quantity, panel);
        if (parsedQuantity == null) return false;

        String validationResult = ValidationUtils.validateId(parsedOrderId);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validateId(productId);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }
        validationResult = ValidationUtils.validateQuantity(parsedQuantity);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }

        // TODO: check if Customer is in a order before deleting
        //  if (orderItemDAOImp.isProductInUse(productId)) {
        //      ("Cannot delete product as it is in use in one or more orders.");
        //  }
        //  instantiate a orderItemDAOImp on constructor

        // TODO: do the thing to update or delete

        try {
            if (!orderItemDAO.deleteOrderItem(parsedOrderId, productId, parsedQuantity)) {
                panel.showErrorMessage("Deleting Order Item failed!");
                return false;
            }
        } catch (InsufficientQuantityException | NegativeQuantityException e) {
            panel.showErrorMessage(e.getMessage());
        }
        return true;
    }

    public ResultSet getOrderItem(String orderId, int productId) {
        Integer parsedId = ParseUtils.parseId(orderId, panel);
        ResultSet rs = orderItemDAO.listOrderItem(parsedId, productId);
        if (rs == null) {
            panel.showErrorMessage("Listing customer failed!");
        }
        return rs;
    }

    public ResultSet getAllOrderItems(String orderId) {
        Integer parsedOrderId = ParseUtils.parseId(orderId, panel);
        if (parsedOrderId == null) return null;

        ResultSet rs = orderItemDAO.listAllOrderItems(parsedOrderId);
        if (rs == null) {
            panel.showErrorMessage("Listing all order items failed!");
        }
        return rs;
    }

    public String calculateTotal(String quant, String unit) {

        Integer parsedQuantity = ParseUtils.parseQuantity(quant, panel, false);
        if (parsedQuantity == null) return "0";
        Float parsedPrice = ParseUtils.parsePrice(unit, panel, false);
        if (parsedPrice == null) return "0";

//        String validationResult = ValidationUtils.validateQuantity(parsedQuantity);
//        if (!validationResult.isEmpty()) {
//            panel.showErrorMessage(validationResult);
//            return "";
//        }
//        validationResult = ValidationUtils.validatePrice(parsedPrice);
//        if (!validationResult.isEmpty()) {
//            panel.showErrorMessage(validationResult);
//            return "";
//        }
        return "" + (parsedQuantity * parsedPrice);
    }
}

