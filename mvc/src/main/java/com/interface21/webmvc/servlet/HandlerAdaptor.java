package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdaptor {

    // DispatcherServlet이 전달한 핸들러가 이 어댑터로 처리 가능한지
    boolean supports(Object handler);

    // 실제 요청을 처리하여 결과 반환
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler);
}
