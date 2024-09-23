package com.interface21.webmvc.servlet.mvc.tobe;

import ch.qos.logback.core.model.Model;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object declaringClass, Method method) {
        this.controller = declaringClass;
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return this.method.invoke(controller,request, response);
    }
}
