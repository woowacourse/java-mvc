package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response);
}
