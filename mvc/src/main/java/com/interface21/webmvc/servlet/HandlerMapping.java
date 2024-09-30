package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    boolean isSupported(HttpServletRequest request);

    Object getHandler(HttpServletRequest request);
}
