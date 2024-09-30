package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(final Object handler);

    ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response);
}
