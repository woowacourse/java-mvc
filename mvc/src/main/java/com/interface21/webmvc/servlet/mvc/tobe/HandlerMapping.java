package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    boolean hasHandler(HttpServletRequest request);

    Object getHandler(HttpServletRequest request);
}
