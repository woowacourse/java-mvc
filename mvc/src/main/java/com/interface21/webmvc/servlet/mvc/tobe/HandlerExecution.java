package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        validateHandlerState();
        
        try {
            final Object result = method.invoke(handler, request, response);
            return convertToModelAndView(result);
        } catch (final Exception e) {
            throw new Exception("Handler execution failed for method: " + method.getName(), e);
        }
    }

    private void validateHandlerState() {
        if (handler == null || method == null) {
            throw new IllegalStateException("Handler or method is null");
        }
    }

    private ModelAndView convertToModelAndView(final Object result) {
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        
        throw new IllegalStateException(
            String.format("Handler method %s must return ModelAndView, but returned: %s", 
                method.getName(), result == null ? "null" : result.getClass().getSimpleName()));
    }
}
