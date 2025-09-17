package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            Object result = method.invoke(handler, request, response);

            if (result instanceof ModelAndView) {
                return (ModelAndView) result;
            }

            throw new IllegalStateException(
                    "Handler method must return ModelAndView, but returned: " +
                            (result != null ? result.getClass().getName() : "null")
            );
        } catch (InvocationTargetException e) {
            throw new Exception("Handler execution failed", e.getCause());
        }
    }
}
