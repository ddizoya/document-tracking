package com.demo.documenttracking.exception;

public class DocumentStoringException extends RuntimeException {

    public DocumentStoringException() {
        super();
    }

    public DocumentStoringException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentStoringException(String message) {
        super(message);
    }

    public DocumentStoringException(Throwable cause) {
        super(cause);
    }
}
