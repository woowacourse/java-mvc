package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE,
    ;

    public static RequestMethod getRequestMethod(String requestMethod) {
        for (RequestMethod value : RequestMethod.values()) {
            if (value.name().equalsIgnoreCase(requestMethod)) {
                return value;
            }
        }
        throw new UnknownHttpMethodException(requestMethod);
    }
}
