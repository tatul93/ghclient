package com.webfontaine.ghclient.exception;

public class WrongParameterException extends RuntimeException {

    public WrongParameterException() {
    }

    public WrongParameterException(String message) {
        super(message);
    }

    public WrongParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongParameterException(Throwable cause) {
        super(cause);
    }

    public WrongParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
