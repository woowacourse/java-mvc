package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        validateMethodContain(controller, method);
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }

    private void validateMethodContain(Object controller, Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (controller.getClass() != declaringClass) {
            throw new IllegalArgumentException("적절하지 않은 컨트롤러와 메서드입니다!");
        }
    }
}
