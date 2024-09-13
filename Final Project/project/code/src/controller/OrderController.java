package controller;

import dao.OrderDAOImpl;
import utils.ParseUtils;
import utils.ValidationUtils;
import view.MyFrame;

import java.sql.ResultSet;

public class OrderController {
    private final MyFrame panel;
    private final OrderDAOImpl orderDAO;

    public OrderController(MyFrame panel) {
        this.panel = panel;
        orderDAO = new OrderDAOImpl();
    }

    public boolean addOrder(final int customerId) {

        String validationResult = ValidationUtils.validateId(customerId);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }


        if (!orderDAO.insertOrder(customerId)) {
            panel.showErrorMessage("Inserting Order failed!");
            return false;
        }
        return true;
    }


    public boolean updateOrder(final String orderId, int customerId) {

        Integer parsedOrderID = ParseUtils.parseId(orderId, panel);
        if (parsedOrderID == null) return false;
        String validationResult = ValidationUtils.validateId(parsedOrderID);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }


        if (!orderDAO.updateOrder(parsedOrderID, customerId)) {
            panel.showErrorMessage("Updating Order failed!");
            return false;
        }
        return true;
    }

    public boolean deleteOrder(final String orderId) {

        Integer parsedOrderId = ParseUtils.parseId(orderId, panel, false);
        if (parsedOrderId == null) return false;

        String validationResult = ValidationUtils.validateId(parsedOrderId);
        if (!validationResult.isEmpty()) {
            panel.showErrorMessage(validationResult);
            return false;
        }

        if (!orderDAO.deleteOrder(parsedOrderId)) {
            panel.showErrorMessage("Deleting Order failed!");
            return false;
        }
        return true;
    }

    public ResultSet getOrder(String orderId) {
        Integer parsedOrderId = ParseUtils.parseId(orderId, panel);
        ResultSet rs = orderDAO.listOrder(parsedOrderId);
        if (rs == null) {
            panel.showErrorMessage("Listing Order failed!");
        }
        return rs;
    }

    public ResultSet getAllOrders() {
        ResultSet rs = orderDAO.listAllOrders();
        if (rs == null) {
            panel.showErrorMessage("Listing all Orders failed!");
        }
        return rs;
    }
}

