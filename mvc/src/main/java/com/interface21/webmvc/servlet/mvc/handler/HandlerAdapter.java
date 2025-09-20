package com.interface21.webmvc.servlet.mvc.handler;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean isHandlingPossible(Object handler);

    ModelAndView handler(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
