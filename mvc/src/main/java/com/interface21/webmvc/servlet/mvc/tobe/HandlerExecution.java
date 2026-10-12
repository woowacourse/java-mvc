package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object result = method.invoke(declaredObject, request, response);
        return toModelAndView(result);
    }

    private ModelAndView toModelAndView(final Object result) {
        if (result instanceof ModelAndView modelAndView) {
            return modelAndView;
        }
        if (result instanceof String viewName) {
            return new ModelAndView(new JspView(viewName));
        }
        throw new IllegalStateException("Unregistered handler type: " + method);
    }
}
