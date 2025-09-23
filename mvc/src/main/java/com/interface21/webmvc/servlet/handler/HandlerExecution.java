package com.interface21.webmvc.servlet.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.view.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object target;
    private final Method method;

    public HandlerExecution(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return this.method.invoke(this.target, request, response);
    }
}
