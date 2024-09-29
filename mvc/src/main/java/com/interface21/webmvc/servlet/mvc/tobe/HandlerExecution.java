package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

public class HandlerExecution {
    private final Object instance;
    private final Method method;

    public HandlerExecution(final Object instance, final Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object result = method.invoke(instance, request, response);

        if (result instanceof String) {
            final String viewName = (String) method.invoke(instance, request, response);
            return new ModelAndView(new JspView(viewName));
        } else if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }

        throw new IllegalArgumentException("메서드의 반환 값이 올바르지 않습니다.");
    }
}
