package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod getRequestMethod(String method) {
        for (var m : RequestMethod.values()) {
            if (m.name().equals(method)) {
                return m;
            }
        }
        throw new IllegalArgumentException("method does no found");
    }
}
