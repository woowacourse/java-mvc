package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Object runnerInstance;
    private final Method handler;

    public HandlerExecution(Object runnerInstance, Method handler) {
        this.runnerInstance = runnerInstance;
        this.handler = handler;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) handler.invoke(runnerInstance, request, response);
    }
}
