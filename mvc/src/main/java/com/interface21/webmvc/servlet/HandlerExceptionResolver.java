package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerExceptionResolver {

    boolean resolveException(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception
    ) throws Exception;
}
