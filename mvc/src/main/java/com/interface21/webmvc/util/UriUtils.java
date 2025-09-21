package com.interface21.webmvc.util;

import jakarta.servlet.http.HttpServletRequest;

public class UriUtils {
    public static String getResourcePath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }
}
