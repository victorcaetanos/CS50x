package utils;

public class ValidationUtils {

    public static String validateManyStrings(final String... strings) {
        for (String s : strings) {
            if (s == null || s.isEmpty()) {
                return "Please enter all fields!";
            }
        }
        return "";
    }

    public static String validateId(final Integer id) {
        final int MIN = 1;
        if (id == null || id < MIN) {
            return "Please enter a valid record!";
        }
        return "";
    }

    public static String validateName(final String name) {
        if (name == null || name.isEmpty()) {
            return "Please enter a name!";
        }
        return "";
    }

    public static String validatePhoneNumber(final String phoneNumber) {
        final int MIN = 10, MAX = 11;
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return "Please enter a Phone Number!";
        } else if (!phoneNumber.matches("[0-9]+")) {
            return "Please enter a Phone Number with only numbers!";
        } else if (phoneNumber.length() < MIN || phoneNumber.length() > MAX) {
            return String.format("Please enter a Phone Number with %s or %s digits!", MIN, MAX);
        }
        return "";
    }

    public static String validateQuantity(final Integer quantity) {
        final int MIN = 1;
        if (quantity == null || quantity < MIN) {
            return String.format("Please enter a quantity that's %s or higher!", MIN);
        }
        return "";
    }

    public static String validateQuantity(final Integer quantity, final int MIN) {
        if (quantity == null || quantity < MIN) {
            return String.format("Please enter a quantity that's %s or higher!", MIN);
        }
        return "";
    }

    public static String validatePrice(final Float price) {
        final Float MIN = 0.0f;
        if (price == null || price <= MIN) {
            return String.format("Please enter a price that's higher than %s!", MIN);
        }
        return "";
    }

    public static String validateEmail(final String email) {
        final String regex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"; // More about the regex at https://regexr.com/3e48o
        if (!email.matches(regex)) {
            return "Please enter a valid Email!";
        }
        return "";
    }
}
