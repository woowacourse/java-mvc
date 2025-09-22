package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE,
    ;

    public static RequestMethod from(String method) {
        if (isAllUpperCase(method)) {
            return RequestMethod.valueOf(method.toUpperCase());
        }
        throw new IllegalArgumentException("Invalid HTTP method: " + method);
    }

    private static boolean isAllUpperCase(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }
}
