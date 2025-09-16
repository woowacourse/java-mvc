package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 어떻게 그 처리기(Handler)를 실행할 것인가
 * DispatcherServlet이 찾아온 다양한 종류의 핸들러를 동일한 방식으로 실행시켜줌
 */
public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
