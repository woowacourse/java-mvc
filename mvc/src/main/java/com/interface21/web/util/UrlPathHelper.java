package com.interface21.web.util;

import jakarta.servlet.http.HttpServletRequest;

public class UrlPathHelper {

    private UrlPathHelper() {
    }

    public static String getPathWithinApplication(final HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }
}
