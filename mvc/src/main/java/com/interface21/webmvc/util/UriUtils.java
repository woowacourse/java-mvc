package com.interface21.webmvc.util;

import jakarta.servlet.http.HttpServletRequest;

public class UriUtils {
    public static String getResourcePath(HttpServletRequest req) {
        String resourcePath = req.getRequestURI().substring(req.getContextPath().length());
        return normalize(resourcePath);
    }

    public static String normalize(String uri) {
        if (uri == null || uri.isEmpty()) {
            return "/";
        }
        // 마지막 / 제거
        if (uri.length() > 1 && uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        // 중복 슬래시 정리
        return uri.replaceAll("//+", "/");
    }
}
