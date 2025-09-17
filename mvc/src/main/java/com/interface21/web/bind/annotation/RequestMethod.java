package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod getRequestMethodBy(String requestMethod) {
        return RequestMethod.valueOf(requestMethod);
    }
}
