package exceptions;

import java.sql.SQLException;

public class NegativeQuantityException extends SQLException {
    public NegativeQuantityException() {
        super("Quantity cannot be less than zero");
    }

    public NegativeQuantityException(String message) {
        super(message);
    }
}
