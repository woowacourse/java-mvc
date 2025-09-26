package com.interface21.webmvc.servlet.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    void initialize();

    Object getHandler(HttpServletRequest request);
}
