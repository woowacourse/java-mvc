package com.interface21.webmvc.servlet.mvc.tobe.handleradapter;

import com.interface21.webmvc.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 핸들러를 실행하기 위한 인터페이스
 */
public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response);
}
