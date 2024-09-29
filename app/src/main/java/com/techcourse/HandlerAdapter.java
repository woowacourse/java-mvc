package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;

    boolean support(Object handler);
}
