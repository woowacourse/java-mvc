package com.interface21.webmvc.servlet.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method handlerMethod;
    private final Object handler;

    public HandlerExecution(
            final Method handlerMethod,
            final Object handler
    ) {
        this.handlerMethod = handlerMethod;
        this.handler = handler;
    }

    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        return (ModelAndView) handlerMethod.invoke(handler, request, response);
    }
}
