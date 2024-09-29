package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Object runnerInstance;
    private final Method handler;

    public HandlerExecution(Object runnerInstance, Method handler) {
        this.runnerInstance = runnerInstance;
        this.handler = handler;
    }

    public Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return handler.invoke(runnerInstance, request, response);
    }

    public Class<?> getReturnType() {
        return handler.getReturnType();
    }
}
