package com.interface21.webmvc.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerExceptionResolver {

    ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws ServletException;

}