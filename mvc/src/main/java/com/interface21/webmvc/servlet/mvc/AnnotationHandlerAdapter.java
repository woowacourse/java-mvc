package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @RequestMapping 메서드를 감싼 HandlerExecution(AnnotationHandlerMapping 이 반환)을 실행하는 어댑터.
 */
public class AnnotationHandlerAdapter implements HandlerAdapter {

    /**
     * HandlerExecution 일 때만 처리한다.
     */
    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    /**
     * HandlerExecution 이 이미 ModelAndView 를 반환하므로 그대로 위임한다.
     */
    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
