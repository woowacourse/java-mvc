package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Method;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Object controllerInstance;
    private final Method method;

    public HandlerExecution(Object controllerInstance, Method method) {
        this.controllerInstance = controllerInstance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object view = method.invoke(controllerInstance, request, response);

        if (view instanceof ModelAndView) {
            return (ModelAndView) view;
        }
        if (view instanceof String viewName) {
            return new ModelAndView(new JspView(viewName));
        }
        throw new IllegalArgumentException("지원되지 않는 반환 타입입니다.");
    }
}
