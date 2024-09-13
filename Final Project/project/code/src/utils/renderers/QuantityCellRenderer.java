package utils.renderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;

public class QuantityCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            String formattedPrice = formatQuantity(value.toString());
            setText(formattedPrice);
        }

        return cellComponent;
    }

    private String formatQuantity(String quantityString) {
        try {
            double quantity = Integer.parseInt(quantityString);
//            return String.format("%.2f", quantity);
            return getQuantityFormat().format(quantity);
        } catch (NumberFormatException e) {
            e.printStackTrace(); // should never happen
            return quantityString;
        }
    }

    public static NumberFormat getQuantityFormat() {
        return NumberFormat.getIntegerInstance();
    }
}
