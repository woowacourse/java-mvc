package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    boolean supports(HttpServletRequest request);

    void initialize();

    Object getHandler(HttpServletRequest request);
}
