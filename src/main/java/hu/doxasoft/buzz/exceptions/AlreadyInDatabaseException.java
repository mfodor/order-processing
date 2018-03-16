package hu.doxasoft.buzz.exceptions;

public class AlreadyInDatabaseException extends Exception {
    public AlreadyInDatabaseException() {
    }

    public AlreadyInDatabaseException(String message) {
        super(message);
    }

    public AlreadyInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyInDatabaseException(Throwable cause) {
        super(cause);
    }

    public AlreadyInDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
