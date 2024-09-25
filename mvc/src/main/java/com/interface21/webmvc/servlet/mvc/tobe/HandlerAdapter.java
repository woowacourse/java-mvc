package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    Object getExecutableHandler(Object handler, HttpServletRequest request, HttpServletResponse response);
}
