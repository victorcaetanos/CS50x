package view;

import controller.CustomerController;
import controller.OrderController;
import controller.OrderItemController;
import controller.ProductController;
import dao.Customer;
import dao.Product;
import utils.renderers.ItemRenderer;
import utils.renderers.PriceCellRenderer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class OrderPanel extends MyFrame {

    private JPanel panelMain;
    private JLabel labelTitleOrder;
    private JTextField fieldOrderID;
    private JLabel labelCustomer;
    private JLabel labelBoxProduct;
    private JLabel labelProductName;
    private JLabel labelProductQuantity;
    private JLabel labelProductUnitPrice;
    private JLabel labelProductTotalPrice;
    private JTextField fieldProductName;
    private JFormattedTextField fieldProductUnitPrice;
    private JFormattedTextField fieldProductQuantity;
    private JFormattedTextField fieldProductTotalPrice;
    private JButton buttonInsertOrder;
    private JButton buttonUpdateOrder;
    private JButton buttonDeleteOrder;
    private JButton buttonInsertOrderItem;
    private JButton buttonDeleteOrderItem;
    private JButton buttonDone;
    private JComboBox<Customer> comboBoxCustomer;
    private JComboBox<Product> comboBoxProduct;
    private JTable tableOrder;
    private JTable tableOrderItem;
    private JScrollPane scrollPanelOrder;
    private JScrollPane scrollPanelOrderItem;
    private JLabel lableTitleItem;

    private final OrderController orderController;
    private final OrderItemController orderItemController;
    private final ProductController productController;
    private final CustomerController customerController;
    private final Vector<String> orderColumnNames = new Vector<>(List.of("Order ID", "Customer ID", "Customer Name", "Total Price"));
    private final Vector<String> orderItemColumnNames = new Vector<>(List.of("Product ID", "Product Name", "Quantity", "Unit Price", "Total Price"));
    private final String EMPTY = "0";

    public OrderPanel() {

        orderController = new OrderController(this);
        orderItemController = new OrderItemController(this);
        productController = new ProductController(this);
        customerController = new CustomerController(this);
        setTableOrderModel(orderController.getAllOrders());
        setTableOrderItemModel(orderItemController.getAllOrderItems(EMPTY));
        populateCustomerComboBox();
        populateProductComboBox();
        comboBoxCustomer.setRenderer(new ItemRenderer());
        comboBoxProduct.setRenderer(new ItemRenderer());

        buttonInsertOrder.addActionListener(e -> {
            Customer customer = (Customer) comboBoxCustomer.getSelectedItem();
            if (orderController.addOrder(customer.getId())) {
                buttonInsertOrder.setEnabled(false);
                comboBoxCustomer.setEnabled(false);
                setItemSectionEnabled(true);
                setTableOrderModel(orderController.getAllOrders());
                fieldOrderID.setText("" + (int) tableOrder.getValueAt(tableOrder.getModel().getRowCount() - 1, 0));
            } else {
                setTableOrderModel(orderController.getAllOrders());
            }
        });

        buttonUpdateOrder.addActionListener(e -> {
            String orderID = fieldOrderID.getText();
            Customer customer = (Customer) comboBoxCustomer.getSelectedItem();
            orderController.updateOrder(orderID, customer.getId());
            setTableOrderModel(orderController.getAllOrders());
        });

        buttonDeleteOrder.addActionListener(e -> {
            int deleteConfirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this item?",
                    "Delete Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (deleteConfirmation == JOptionPane.NO_OPTION) {
                return;
            }
            String orderID = fieldOrderID.getText();
            if (orderController.deleteOrder(orderID)) {
                doneAction();
            }
            setTableOrderModel(orderController.getAllOrders());
            setTableOrderItemModel(orderItemController.getAllOrderItems(EMPTY));
        });

        comboBoxProduct.addActionListener(e -> {
            Product product = (Product) comboBoxProduct.getSelectedItem();

            if (product == null) {
                return;
            }
            if (comboBoxProduct.getSelectedIndex() == 0) {
                buttonInsertOrderItem.setEnabled(false);
                buttonDeleteOrderItem.setEnabled(false);
                fieldProductQuantity.setEditable(false);
                fieldProductName.setText("");
                fieldProductUnitPrice.setText("");
            } else {
                buttonInsertOrderItem.setEnabled(true);
                buttonDeleteOrderItem.setEnabled(true);
                fieldProductQuantity.setEditable(true);
                fieldProductName.setText(product.getName());
                fieldProductUnitPrice.setText("" + product.getPrice());
            }
        });

        fieldProductQuantity.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setTotalPrice();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setTotalPrice();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setTotalPrice();
            }

            private void setTotalPrice() {
                String quant = fieldProductQuantity.getText();
                String unit = fieldProductUnitPrice.getText();
                fieldProductTotalPrice.setText(orderItemController.calculateTotal(quant, unit));
            }
        });

        buttonInsertOrderItem.addActionListener(e -> {
            Product product = (Product) comboBoxProduct.getSelectedItem();
            String orderID = fieldOrderID.getText();
            String itemQuantity = fieldProductQuantity.getText();
            orderItemController.addOrderItem(orderID, product.getId(), itemQuantity, product.getPrice());
            setTableOrderItemModel(orderItemController.getAllOrderItems(orderID));
            setTableOrderModel(orderController.getAllOrders());
        });

        buttonDeleteOrderItem.addActionListener(e -> {
            int deleteConfirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this item?",
                    "Delete Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (deleteConfirmation == JOptionPane.NO_OPTION) {
                return;
            }

            Product product = (Product) comboBoxProduct.getSelectedItem();
            String orderID = fieldOrderID.getText();
            String itemQuantity = fieldProductQuantity.getText();
            orderItemController.deleteOrderItem(orderID, product.getId(), itemQuantity);
            setTableOrderItemModel(orderItemController.getAllOrderItems(orderID));
            setTableOrderModel(orderController.getAllOrders());
        });

        tableOrder.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                int selectedRow = tableOrder.getSelectedRow();
                if (selectedRow < 0) {
                    return;
                }

                String orderId = "" + (int) tableOrder.getValueAt(selectedRow, 0);
                int customerId = (int) tableOrder.getValueAt(selectedRow, 1);

                Vector<Customer> customers = customerController.getAllCustomersVector();
                for (Customer customer : customers) {
                    if (customer.getId() == customerId) {
                        comboBoxCustomer.setSelectedItem(customer);
                        break;
                    }
                }

                ResultSet rs = orderController.getOrder(orderId);
                if (rs == null) {
                    return;
                }

                if (!buttonDone.isEnabled()) {
                    buttonInsertOrder.setEnabled(false);
                    buttonUpdateOrder.setEnabled(true);
                    buttonDeleteOrder.setEnabled(true);
                    comboBoxCustomer.setEnabled(true);
                    buttonDone.setEnabled(true);
                    setItemSectionEnabled(true);
                }
                fieldOrderID.setText(orderId);
                setTableOrderModel(orderController.getAllOrders());
                setTableOrderItemModel(orderItemController.getAllOrderItems(orderId));
            }
        });

        tableOrderItem.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableOrderItem.getSelectedRow();
                if (selectedRow >= 0) {
                    String orderId = fieldOrderID.getText();
                    int productId = (int) tableOrderItem.getValueAt(selectedRow, 0);

                    Vector<Product> products = productController.getAllProductsVector();
                    for (Product product : products) {
                        if (product.getId() == productId) {
                            comboBoxProduct.setSelectedItem(product);
                            break;
                        }
                    }

                    ResultSet rs = orderItemController.getOrderItem(orderId, productId);
                    if (rs == null) {
                        return;
                    }
                }
            }
        });

        buttonDone.addActionListener(e -> {
            doneAction();
            comboBoxCustomer.setEnabled(true);
            setTableOrderItemModel(orderItemController.getAllOrderItems(EMPTY));
        });

        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void doneAction() {
        clearAllFields();
        buttonInsertOrder.setEnabled(true);
        buttonUpdateOrder.setEnabled(false);
        buttonDeleteOrder.setEnabled(false);
        setItemSectionEnabled(false);
    }

    public void setTableOrderModel(final ResultSet rs) {
        if (rs == null) return;

        try {
            tableOrder.setModel(buildTableModel(rs, orderColumnNames));
            tableOrder.getColumnModel().getColumn(3).setCellRenderer(new PriceCellRenderer());
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTableOrderItemModel(final ResultSet rs) {
        if (rs == null) return;

        try {
            tableOrderItem.setModel(buildTableModel(rs, orderItemColumnNames));
            tableOrderItem.getColumnModel().getColumn(3).setCellRenderer(new PriceCellRenderer());
            tableOrderItem.getColumnModel().getColumn(4).setCellRenderer(new PriceCellRenderer());
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAllFields() {
        fieldOrderID.setText("");
        fieldProductQuantity.setText("");
        comboBoxCustomer.setSelectedIndex(0);
        comboBoxProduct.setSelectedIndex(0);
        fieldProductUnitPrice.setText("");
        fieldProductTotalPrice.setText("");
        fieldProductName.setText("");
    }

    private void setItemSectionEnabled(boolean b) {
        comboBoxProduct.setEnabled(b);
        buttonDone.setEnabled(b);
    }

    public void populateCustomerComboBox() {
        Vector<Customer> customers = customerController.getAllCustomersVector();
        comboBoxCustomer.addItem(new Customer(0, "Choose a Customer"));
        for (Customer customer : customers) {
            comboBoxCustomer.addItem(customer);
        }
    }

    public void populateProductComboBox() {
        Vector<Product> products = productController.getAllProductsVector();
        comboBoxProduct.addItem(new Product(0, "Choose a Product"));
        for (Product product : products) {
            comboBoxProduct.addItem(product);
        }
    }
}
