package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    private static final RequestMethod[] REQUEST_METHODS = values();

    public static RequestMethod[] getRequestMethods() {
        return REQUEST_METHODS;
    }
}
