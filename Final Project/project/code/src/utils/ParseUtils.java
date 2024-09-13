package utils;

import view.MyFrame;

public class ParseUtils {

    public static Integer parseId(final String id, final MyFrame panel, boolean hasWarning) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            if (hasWarning)
                panel.showErrorMessage("Invalid ID format. Please enter only numeric values.");
            return null;
        }
    }

    public static Integer parseId(final String id, final MyFrame panel) {
        return parseId(id, panel, true);
    }

    public static String parsePhoneNumber(final String phoneNumber, final MyFrame panel, boolean hasWarning) {

        final String regex = java.util.regex.Matcher.quoteReplacement("[()_-]");
        final String replacement = "";
        try {
            return phoneNumber.replaceAll(regex, replacement);
        } catch (NumberFormatException e) {
            if (hasWarning)
                panel.showErrorMessage("Invalid Phone Number format. Please enter only numeric values.");
            return null;
        }
    }

    public static String parsePhoneNumber(final String phoneNumber, final MyFrame panel) {
        return parsePhoneNumber(phoneNumber, panel, true);
    }

    public static Integer parseQuantity(final String quantity, final MyFrame panel, boolean hasWarning) {

        final String regex = java.util.regex.Matcher.quoteReplacement(",");
        final String replacement = "";
        try {
            String cleanQuantity = quantity.replaceAll(regex, replacement);
            return Integer.parseInt(cleanQuantity);
        } catch (NumberFormatException e) {
            if (hasWarning)
                panel.showErrorMessage("Invalid Quantity format. Please enter only numeric values.");
            return null;
        }
    }

    public static Integer parseQuantity(final String quantity, final MyFrame panel) {
        return parseQuantity(quantity, panel, true);
    }

    public static Float parsePrice(final String price, final MyFrame panel, boolean hasWarning) {

        final String regex = java.util.regex.Matcher.quoteReplacement("[,]");
        final String replacement = "";
        try {
            String cleanPrice = price.replaceAll(regex, replacement);
            return Float.parseFloat(cleanPrice);
        } catch (NumberFormatException e) {
            if (hasWarning)
                panel.showErrorMessage("Invalid Price format. Please enter only numeric values.");
            return null;
        }
    }

    public static Float parsePrice(final String price, final MyFrame panel) {
        return parsePrice(price, panel, true);
    }
}

