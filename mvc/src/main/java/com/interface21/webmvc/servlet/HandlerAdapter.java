package com.interface21.webmvc.servlet;

import java.lang.reflect.InvocationTargetException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws IllegalAccessException, InvocationTargetException;
}
