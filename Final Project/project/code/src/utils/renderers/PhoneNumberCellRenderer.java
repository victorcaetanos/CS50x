package utils.renderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PhoneNumberCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            String formattedPhoneNumber = formatPhoneNumber(value.toString());
            setText(formattedPhoneNumber);
        }

        return cellComponent;
    }

    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() == 10) {
            return String.format("(%s)_%s-%s", phoneNumber.substring(0, 2), phoneNumber.substring(2, 6), phoneNumber.substring(6));
        } else if (phoneNumber.length() == 11) {
            return String.format("(%s)%s-%s", phoneNumber.substring(0, 2), phoneNumber.substring(2, 7), phoneNumber.substring(7));
        }
        return phoneNumber; // should never happen
    }
}

