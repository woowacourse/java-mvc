package com.interface21.webmvc.servlet.mvc.handler.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception;
}
