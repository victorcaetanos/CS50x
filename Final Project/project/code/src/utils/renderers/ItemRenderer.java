package utils.renderers;

import dao.Customer;
import dao.Product;

import javax.swing.*;
import java.awt.*;

public class ItemRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Product product) {
            setText(product.getName() + " (ID: " + product.getId() + ")");
            if (product.getId() == 0) {
                setText(product.getName());
            }
        }
        if (value instanceof Customer customer) {
            setText(customer.getName() + " (ID: " + customer.getId() + ")");
            if (customer.getId() == 0) {
                setText(customer.getName());
            }
        }
        return this;
    }

}
