package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean isSupported(Object handler);

    ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, Object handler);
}
