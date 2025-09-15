package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object instance;
    private final Method executeMethod;

    public HandlerExecution(
            final Object instance,
            final Method executeMethod
    ) {
        this.instance = instance;
        this.executeMethod = executeMethod;
    }

    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        return (ModelAndView) executeMethod.invoke(instance, request, response);
    }
}
