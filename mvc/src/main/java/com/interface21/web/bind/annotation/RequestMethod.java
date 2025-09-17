package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod getRequestMethod(String method) {
        for (var requestMethod : RequestMethod.values()) {
            if (requestMethod.name().equals(method)) {
                return requestMethod;
            }
        }
        throw new IllegalArgumentException("method does no found");
    }
}
