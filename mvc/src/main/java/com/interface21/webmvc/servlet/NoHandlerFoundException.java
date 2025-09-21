package com.interface21.webmvc.servlet;

import jakarta.servlet.ServletException;

public class NoHandlerFoundException extends ServletException {

    public NoHandlerFoundException(String httpMethod, String requestURL) {
        super("No endpoint " + httpMethod + " " + requestURL + ".");
    }
}
