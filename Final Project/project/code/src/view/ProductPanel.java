package view;

import controller.ProductController;
import utils.renderers.PriceCellRenderer;
import utils.renderers.QuantityCellRenderer;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import static utils.renderers.PriceCellRenderer.getPriceFormat;
import static utils.renderers.QuantityCellRenderer.getQuantityFormat;


public class ProductPanel extends MyFrame {

    private JPanel panelMain;
    private JLabel labelTitle;
    private JLabel labelID;
    private JLabel labelName;
    private JLabel labelPrice;
    private JLabel labelQuantity;
    private JTextField fieldID;
    private JTextField fieldName;
    private JFormattedTextField fieldPrice;
    private JFormattedTextField fieldQuantity;
    private JButton buttonInsert;
    private JButton buttonDelete;
    private JButton buttonUpdate;
    private JButton buttonDone;
    private JTable tableProduct;
    private JScrollPane scrollPanelProduct;

    private final ProductController productController;
    private final Vector<String> productColumnNames = new Vector<>(List.of("Product ID", "Name", "Price", "Quantity"));

    public ProductPanel() {

        productController = new ProductController(this);
        setTableModel(productController.getAllProducts());

        buttonInsert.addActionListener(e -> {
            String name = fieldName.getText();
            String price = fieldPrice.getText();
            String quantity = fieldQuantity.getText();
            if (productController.addProduct(name, price, quantity)) {
                clearAllFields();
            }
            setTableModel(productController.getAllProducts());
        });

        buttonUpdate.addActionListener(e -> {
            String id = fieldID.getText();
            String name = fieldName.getText();
            String phoneNumber = fieldPrice.getText();
            String email = fieldQuantity.getText();
            if (productController.updateProduct(id, name, phoneNumber, email)) {
                clearAllFields();
                switchButtons(false);
            }
            setTableModel(productController.getAllProducts());
        });

        buttonDelete.addActionListener(e -> {
            int deleteConfirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this item?",
                    "Delete Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (deleteConfirmation == JOptionPane.NO_OPTION) {
                return;
            }

            String id = fieldID.getText();
            if (productController.deleteProduct(id)) {
                clearAllFields();
                switchButtons(false);
            }
            setTableModel(productController.getAllProducts());
        });

        tableProduct.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableProduct.getSelectedRow();
                if (selectedRow >= 0) {
                    String productId = "" + (int) tableProduct.getValueAt(selectedRow, 0);

                    ResultSet rs = productController.getProduct(productId);
                    if (rs == null) {
                        return;
                    }

                    if (!buttonDone.isEnabled()) {
                        switchButtons(true);
                    }
                    fieldID.setText(productId);
                    setFieldTexts(rs);
                }
            }
        });

        buttonDone.addActionListener(e -> {
            clearAllFields();
            setTableModel(productController.getAllProducts());
            switchButtons(false);
        });

        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public void switchButtons(boolean b) {
        buttonInsert.setEnabled(!b);
        buttonUpdate.setEnabled(b);
        buttonDelete.setEnabled(b);
        buttonDone.setEnabled(b);
    }

    public void setTableModel(final ResultSet rs) {
        if (rs == null) return;

        try {
            tableProduct.setModel(buildTableModel(rs, productColumnNames));
            tableProduct.getColumnModel().getColumn(2).setCellRenderer(new PriceCellRenderer());
            tableProduct.getColumnModel().getColumn(3).setCellRenderer(new QuantityCellRenderer());
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFieldTexts(final ResultSet rs) {
        if (rs == null) return;

        try {
            if (!rs.next()) return;
            fieldName.setText(rs.getString("name"));
            fieldPrice.setText(rs.getString("price"));
            fieldQuantity.setText(rs.getString("quantity"));
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAllFields() {
        fieldName.setText("");
        fieldPrice.setText("0");
        fieldQuantity.setText("0");
        fieldID.setText("");
    }

    private void createUIComponents() {

        NumberFormatter currencyFormatter = new NumberFormatter(getPriceFormat());
        currencyFormatter.setValueClass(Double.class);
        currencyFormatter.setAllowsInvalid(false);
        currencyFormatter.setMinimum(0.0);
        currencyFormatter.setMaximum(10_000_000.0);
        fieldPrice = new JFormattedTextField(currencyFormatter);


        NumberFormatter quantityFormatter = new NumberFormatter(getQuantityFormat());
        quantityFormatter.setValueClass(Integer.class);
        quantityFormatter.setAllowsInvalid(false);
        quantityFormatter.setMinimum(0);
        quantityFormatter.setMaximum(Integer.MAX_VALUE);
        fieldQuantity = new JFormattedTextField(quantityFormatter);
    }
}