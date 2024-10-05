package com.interface21.webmvc.servlet.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    Object getHandler(HttpServletRequest request);

    boolean supports(HttpServletRequest request);
}
