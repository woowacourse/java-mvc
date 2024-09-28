package com.interface21.webmvc.servlet.mvc.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    void init();

    Object getHandler(HttpServletRequest request);
}
