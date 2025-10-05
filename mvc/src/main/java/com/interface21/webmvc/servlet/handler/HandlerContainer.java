package com.interface21.webmvc.servlet.handler;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerContainer {

    void initialize(Object... basePackages);

    Object getHandler(HttpServletRequest request);
}
