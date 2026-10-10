package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(Object handler);

    /**
     * Execute a handler for which supports(handler) returned true.
     */
    ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
