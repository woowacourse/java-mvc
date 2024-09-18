package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object instance;
    private final Method method;

    public HandlerExecution(final Method method, final Object instance) {
        this.method = method;
        this.instance = instance;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, method);
    }
}
