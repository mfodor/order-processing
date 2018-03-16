package hu.doxasoft.buzz.exceptions;

public class InvalidInputFileException extends Exception {
    public InvalidInputFileException() {
    }

    public InvalidInputFileException(String message) {
        super(message);
    }

    public InvalidInputFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputFileException(Throwable cause) {
        super(cause);
    }

    public InvalidInputFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
