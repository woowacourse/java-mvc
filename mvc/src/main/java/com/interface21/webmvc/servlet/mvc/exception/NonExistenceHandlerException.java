package com.interface21.webmvc.servlet.mvc.exception;

public class NonExistenceHandlerException extends RuntimeException {

    private static final String MESSAGE = "Not Mapped Handler";

    public NonExistenceHandlerException(String request) {
        super(MESSAGE + " : " + request);
    }
}
