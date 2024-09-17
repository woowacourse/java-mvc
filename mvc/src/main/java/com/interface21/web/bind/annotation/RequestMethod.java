package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod findMethod(String method) {
        return RequestMethod.valueOf(method.toUpperCase());
    }
}
