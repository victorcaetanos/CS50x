package exceptions;

import java.sql.SQLException;

public class InsufficientQuantityException extends SQLException {
    public InsufficientQuantityException() {
        super("Quantity higher than available stock");
    }

    public InsufficientQuantityException(String message) {
        super(message);
    }
}
