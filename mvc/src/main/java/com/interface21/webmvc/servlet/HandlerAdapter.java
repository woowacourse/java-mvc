package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
컨트롤러에서 반환 값을 받아서, ModelAndView로 변환한다.
 */
public interface HandlerAdapter {

    boolean support(Object handler);

    ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
