package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.exception.HandlerExecutionException;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object instance;

    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object view = method.invoke(instance, request, response);

        if (view instanceof ModelAndView) {
            return (ModelAndView) view;
        }
        if (view instanceof String viewName) {
            return new ModelAndView(new JspView(viewName));
        }
        throw new HandlerExecutionException();
    }
}
