package com.interface21.webmvc.servlet.controller.handler;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    HandlerExecution getHandler(final HttpServletRequest request);
}
