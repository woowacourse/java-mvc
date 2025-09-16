package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod getByName(final String method) {
        for (RequestMethod value : RequestMethod.values()) {
            if (value.name().equalsIgnoreCase(method.trim())) {
                return value;
            }
        }
        throw new IllegalArgumentException("잘못된 Http Method입니다. : " + method);
    }

    public static RequestMethod[] allValues() {
        return values();
    }
}
