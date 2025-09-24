package com.interface21.webmvc.servlet.mvc.handler.adapter;

import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean isHandlingPossible(Object handler);

    ModelAndView handler(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
