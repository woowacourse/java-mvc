package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        Object result = method.invoke(controller, request, response);
        if (result instanceof ModelAndView modelAndView) {
            return modelAndView;
        }

        throw new IllegalStateException("핸들러 메서드가 ModelAndView를 반환하지 않았습니다: " + method.toGenericString());
    }
}
