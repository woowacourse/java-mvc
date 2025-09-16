package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        try {
            final Object result = method.invoke(handler, request, response);

            if (result instanceof ModelAndView mav) {
                return mav;
            }
            return null;
        } catch (InvocationTargetException e) {
            throw (Exception) e.getTargetException();
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
}
