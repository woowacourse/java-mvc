package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(Class<?> handlerType);

    ModelAndView handle(
            Object handler,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception;
}
