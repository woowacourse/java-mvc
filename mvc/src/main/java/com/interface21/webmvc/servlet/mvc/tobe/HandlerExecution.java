package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    private Object controllerClass;
    private Method method;

    public HandlerExecution(Object controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controllerClass, request, response);
    }
}
