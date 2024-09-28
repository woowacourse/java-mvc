package com.interface21.web.bind.annotation;

public enum RequestMethod {

    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE,
    ;

    public static RequestMethod from(String method) {
        try {
            return RequestMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Wrong http method - given: " + method);
        }
    }
}
