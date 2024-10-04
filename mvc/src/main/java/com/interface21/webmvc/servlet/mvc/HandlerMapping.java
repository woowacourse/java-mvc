package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    Handler getHandler(HttpServletRequest request);

    boolean canHandle(HttpServletRequest request);
}
