package com.webfontaine.ghclient.exception;

public class DocumentAlreadyExistException extends RuntimeException {

    public DocumentAlreadyExistException(String message) {
        super(message);
    }

    public DocumentAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentAlreadyExistException(Throwable cause) {
        super(cause);
    }

    protected DocumentAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
