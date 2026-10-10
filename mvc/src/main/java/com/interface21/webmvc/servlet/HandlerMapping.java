package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    default int getOrder() {
        return 0;
    }

    Object getHandler(HttpServletRequest request);
}
