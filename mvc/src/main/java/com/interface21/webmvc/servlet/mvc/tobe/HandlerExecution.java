package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);
    private final Object controllerInstance;
    private final Method method;

    public HandlerExecution(Object controllerInstance, Method method) {
        this.controllerInstance = controllerInstance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object result = method.invoke(controllerInstance, request, response);
        if (result instanceof ModelAndView modelAndView) {
            return modelAndView;
        }
        if (result instanceof String viewName) {
            return new ModelAndView(viewName);
        }
        throw new IllegalStateException("지원하지 않는 반환 타입입니다: " + method);
    }
}
