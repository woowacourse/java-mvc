package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    boolean handleable(Object handler);

    ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception;
}
