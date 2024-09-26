package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.exception.ControllerExecutionException;
import com.interface21.webmvc.servlet.mvc.tobe.exception.ControllerResultCastException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(controller, request, response);

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ControllerExecutionException(e.getMessage());

        } catch (ClassCastException e) {
            throw new ControllerResultCastException(method.getName());
        }
    }
}
