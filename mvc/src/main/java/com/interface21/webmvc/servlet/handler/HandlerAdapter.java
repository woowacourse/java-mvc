package com.interface21.webmvc.servlet.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdapter.class);

    public static Object executeHandler(Object handler, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException {
        try {
            if (handler instanceof HandlerExecution) {
                return ((HandlerExecution) handler).handle(httpServletRequest, httpServletResponse);
            } else {
                throw new ServletException("지원하지 않는 응답 방식입니다.");
            }
        } catch (Exception exception) {
            log.error("Exception : {}", exception.getMessage(), exception);
            throw new ServletException(exception.getMessage());
        }
    }
}
