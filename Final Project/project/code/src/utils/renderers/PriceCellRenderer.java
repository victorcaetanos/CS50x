package utils.renderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

public class PriceCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            String formattedPrice = formatPrice(value.toString());
            setText(formattedPrice);
        }

        return cellComponent;
    }

    private String formatPrice(String priceString) {
        try {
            double price = Float.parseFloat(priceString);
//            return String.format("R$ %.2f", price);
            return getPriceFormat().format(price);
        } catch (NumberFormatException e) {
            e.printStackTrace(); // should never happen
            return priceString;
        }
    }

    public static DecimalFormat getPriceFormat() {
        String pattern = "#,##0.00";
        DecimalFormat currencyFormat = new DecimalFormat(pattern);
        currencyFormat.setDecimalSeparatorAlwaysShown(true);
        return currencyFormat;
    }
}

