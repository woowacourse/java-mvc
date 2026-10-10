package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(Object handler, Method method) {
        validate(method);
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            return (ModelAndView) method.invoke(handler, request, response);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception ex) throw ex;
            throw e;
        }
    }

    private void validate(Method method) {
        if (!ModelAndView.class.isAssignableFrom(method.getReturnType())) {
            throw new IllegalStateException("핸들러 반환 타입은 ModelAndView여야 합니다: " + method);
        }
        Class<?>[] params = method.getParameterTypes();
        if (params.length != 2
            || !params[0].isAssignableFrom(HttpServletRequest.class)
            || !params[1].isAssignableFrom(HttpServletResponse.class)) {
            throw new IllegalStateException("핸들러 파라미터는 (HttpServletRequest, HttpServletResponse)여야 합니다: " + method);
        }
    }
}
