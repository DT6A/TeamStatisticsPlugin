package ru.hse.plugin;

public class WeNeedNameException extends Exception {

    public WeNeedNameException() {
    }

    public WeNeedNameException(String message) {
        super(message);
    }

    public WeNeedNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeNeedNameException(Throwable cause) {
        super(cause);
    }

    public WeNeedNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
