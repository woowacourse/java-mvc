package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.MethodInvocationException;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        final Method method = (Method) handler;
        return Controller.class.isAssignableFrom(method.getDeclaringClass());
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) {
        final String viewName = invokeHandler(request, response, handler);
        final View view = new JspView(viewName);
        return new ModelAndView(view);
    }

    private String invokeHandler(final HttpServletRequest request, final HttpServletResponse response,
                                 final Object handler) {
        try {
            final HandlerExecution handlerExecution = (HandlerExecution) handler;
            final Object bean = handlerExecution.getBean();
            final Method method = (Method) handlerExecution.getMethod();

            return (String) method.invoke(bean, request, response);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new MethodInvocationException("핸들러 메소드 실행 중 예외가 발생했습니다.", e);
        }
    }
}
