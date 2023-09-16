package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMethod;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        Method method = (Method) handler;
        return Controller.class.isAssignableFrom(method.getDeclaringClass());
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String viewName = invokeHandler(request, response, handler);
        View view = new JspView(viewName);

        return new ModelAndView(view);
    }

    private String invokeHandler(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = (Method) handlerMethod.getHandler();
            Object bean = handlerMethod.getBean();

            return (String) method.invoke(bean, request, response);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
