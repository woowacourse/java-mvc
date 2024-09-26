package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    ModelAndView invoke(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
