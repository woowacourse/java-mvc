package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handlerMethod;
    private final Object handler;

    public HandlerExecution(
            final Object handlerMethod,
            final Object handler
    ) {
        this.handlerMethod = handlerMethod;
        this.handler = handler;
    }

    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final Method method = (Method) handlerMethod;
        final Object returnValue = method.invoke(handler, request, response);

        return (ModelAndView) returnValue;
    }
}
