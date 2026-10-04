package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object target;

    public HandlerExecution(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object invoked = method.invoke(target, request, response);
        return (ModelAndView) invoked;
    }
}
