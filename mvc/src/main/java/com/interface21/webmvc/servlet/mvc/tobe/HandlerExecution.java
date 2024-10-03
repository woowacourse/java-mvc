package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handler;

    private final Method method;

    public HandlerExecution(Object handler, Method hadlerMethod) {
        this.handler = handler;
        this.method = hadlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object returnValue = method.invoke(handler, request, response);
        if (returnValue instanceof ModelAndView) {
            return (ModelAndView) returnValue;
        }
        return new ModelAndView((new JspView((String) returnValue)));
    }
}
