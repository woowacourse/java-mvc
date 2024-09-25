package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    boolean supports(Object handler);

    String handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
