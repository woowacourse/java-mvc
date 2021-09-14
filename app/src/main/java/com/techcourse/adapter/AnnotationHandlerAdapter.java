package com.techcourse.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.InvocationTargetException;

public class AnnotationHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ClassNotFoundException {
        try {
            return ((HandlerExecution) handler).handle(request, response);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ClassNotFoundException("연결된 어댑터를 찾을 수 없습니다.");
        }
    }
}
