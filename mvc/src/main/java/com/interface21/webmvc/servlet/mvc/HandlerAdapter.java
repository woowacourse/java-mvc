package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

public interface HandlerAdapter {

    /**
     * 주어진 핸들러를 이 어댑터가 지원하는지 확인한다.
     * @param handler 핸들러 객체
     * @return 지원 여부
     */
    boolean supports(Object handler);

    /**
     * 핸들러를 실행하고 ModelAndView를 반환한다.
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param handler 핸들러 객체
     * @return ModelAndView
     * @throws Exception 핸들러 실행 중 예외
     */
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}