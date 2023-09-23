package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdaptor {
    @Override
    public boolean supports(final Object handler) {
        final Method method = (Method) handler;
        return Controller.class.isAssignableFrom(method.getDeclaringClass());
    }

    @Override
    public ModelAndView handle(
            final HandlerExecution handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        final String viewName = invokeHandler(handler, request, response);
        final JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }

    private String invokeHandler(
            final HandlerExecution handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        try {
            return (String) handler.handle(request, response);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new CommonMethodInvokeException("메서드 실행 도중 예외가 발생했습니다.", e);
        }
    }
}
