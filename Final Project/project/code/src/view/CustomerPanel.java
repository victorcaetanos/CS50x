package view;

import controller.CustomerController;
import utils.renderers.PhoneNumberCellRenderer;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;


public class CustomerPanel extends MyFrame {

    private JPanel panelMain;
    private JLabel labelTitle;
    private JLabel labelID;
    private JLabel labelName;
    private JLabel labelPhoneNumber;
    private JLabel labelEmail;
    private JTextField fieldID;
    private JTextField fieldName;
    private JFormattedTextField fieldPhoneNumber;
    private JFormattedTextField fieldEmail;
    private JButton buttonInsert;
    private JButton buttonDelete;
    private JButton buttonUpdate;
    private JButton buttonDone;
    private JTable tableCustomer;
    private JScrollPane scrollPanelCustomer;

    private final CustomerController customerController;
    private final Vector<String> customerColumnNames = new Vector<>(List.of("Customer ID", "Name", "Phone Number", "Email"));

    public CustomerPanel() {


        customerController = new CustomerController(this);
        setTableModel(customerController.getAllCustomers());

        buttonInsert.addActionListener(e -> {
            String name = fieldName.getText();
            String phoneNumber = fieldPhoneNumber.getText();
            String email = fieldEmail.getText();
            if (customerController.addCustomer(name, phoneNumber, email)) {
                clearAllFields();
            }
            setTableModel(customerController.getAllCustomers());
        });

        buttonUpdate.addActionListener(e -> {
            String id = fieldID.getText();
            String name = fieldName.getText();
            String phoneNumber = fieldPhoneNumber.getText();
            String email = fieldEmail.getText();
            if (customerController.updateCustomer(id, name, phoneNumber, email)) {
                clearAllFields();
                switchButtons(false);
            }
            setTableModel(customerController.getAllCustomers());
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
            if (customerController.deleteCustomer(id)) {
                clearAllFields();
                switchButtons(false);
            }
            setTableModel(customerController.getAllCustomers());
        });

        tableCustomer.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableCustomer.getSelectedRow();
                if (selectedRow >= 0) {
                    String customerId = "" + (int) tableCustomer.getValueAt(selectedRow, 0);

                    ResultSet rs = customerController.getCustomer(customerId);
                    if (rs == null) {
                        return;
                    }

                    if (!buttonDone.isEnabled()) {
                        switchButtons(true);
                    }
                    fieldID.setText(customerId);
                    setFieldTexts(rs);
                }
            }
        });

        buttonDone.addActionListener(e -> {
            clearAllFields();
            setTableModel(customerController.getAllCustomers());
            switchButtons(false);
        });

        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void switchButtons(boolean b) {
        buttonInsert.setEnabled(!b);
        buttonUpdate.setEnabled(b);
        buttonDelete.setEnabled(b);
        buttonDone.setEnabled(b);
    }

    public void setTableModel(final ResultSet rs) {
        if (rs == null) return;

        try {
            tableCustomer.setModel(buildTableModel(rs, customerColumnNames));
            tableCustomer.getColumnModel().getColumn(2).setCellRenderer(new PhoneNumberCellRenderer());
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
            fieldPhoneNumber.setText(rs.getString("phoneNumber"));
            fieldEmail.setText(rs.getString("email"));
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAllFields() {
        fieldName.setText("");
        fieldPhoneNumber.setText("");
        fieldEmail.setText("");
        fieldID.setText("");
    }

    private void createUIComponents() {
        MaskFormatter phoneFormatter = null;
        String pattern = "(**)*****-****";
        char placeholder = '_';
        try {
            phoneFormatter = new MaskFormatter(pattern);
            phoneFormatter.setPlaceholderCharacter(placeholder);
        } catch (ParseException e) {
            System.err.println("ParseException: " + e.getMessage());
            showErrorMessage("A Phone Number might not be well formatted");
        }
        fieldPhoneNumber = new JFormattedTextField(phoneFormatter);
    }
}