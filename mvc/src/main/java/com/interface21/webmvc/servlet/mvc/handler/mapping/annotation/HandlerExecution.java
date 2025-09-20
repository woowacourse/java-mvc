package com.interface21.webmvc.servlet.mvc.handler.mapping.annotation;

import com.interface21.webmvc.servlet.mvc.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handler;
    private final Method handlerMethod;

    public HandlerExecution(final Object handler, final Method handlerMethod) {
        this.handler = handler;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            return (ModelAndView) handlerMethod.invoke(handler, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Handler method invocation failed", e);
        }
    }

    @Override
    public String toString() {
        return String.format("method: %s#%s",
                handlerMethod.getDeclaringClass().getSimpleName(),
                handlerMethod.getName());
    }
}
