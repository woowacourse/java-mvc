package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(Object handler);

    Object handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
