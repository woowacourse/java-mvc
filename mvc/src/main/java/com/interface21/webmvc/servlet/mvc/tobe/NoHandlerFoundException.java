package com.interface21.webmvc.servlet.mvc.tobe;

public class NoHandlerFoundException extends RuntimeException {

    private final String httpMethod;
    private final String requestURL;

    public NoHandlerFoundException(String httpMethod, String requestURL) {
        super(String.format("No handler found for %s %s", httpMethod, requestURL));
        this.httpMethod = httpMethod;
        this.requestURL = requestURL;
    }
}
