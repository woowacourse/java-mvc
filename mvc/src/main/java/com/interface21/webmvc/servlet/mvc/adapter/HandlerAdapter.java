package com.interface21.webmvc.servlet.mvc.adapter;

import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean canHandle(Object handler);

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
