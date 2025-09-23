package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Object controllerInstance;
    private final Method handlerMethod;

    public HandlerExecution(Object controllerInstance, Method handlerMethod) {
        this.controllerInstance = controllerInstance;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object result = handlerMethod.invoke(controllerInstance, request, response);
        
        if (result instanceof String) {
            String viewName = (String) result;
            return new ModelAndView(new JspView(viewName));
        } else if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        
        throw new IllegalArgumentException("Handler method must return String or ModelAndView");
    }
}
