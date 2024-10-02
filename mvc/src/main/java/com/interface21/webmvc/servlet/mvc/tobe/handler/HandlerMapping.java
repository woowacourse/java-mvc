package com.interface21.webmvc.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    HandlerExecution getHandler(HttpServletRequest request);
}
