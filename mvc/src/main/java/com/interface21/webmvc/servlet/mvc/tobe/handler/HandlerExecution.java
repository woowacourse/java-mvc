package com.interface21.webmvc.servlet.mvc.tobe.handler;

import java.lang.reflect.Method;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object result = method.invoke(instance, request, response);
        if (result instanceof String) {
            return new ModelAndView((String) result);
        }
        return (ModelAndView) result;
    }
}
