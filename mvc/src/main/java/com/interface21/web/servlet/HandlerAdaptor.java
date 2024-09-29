package com.interface21.web.servlet;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdaptor {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

    boolean supports(Object handler);
}
