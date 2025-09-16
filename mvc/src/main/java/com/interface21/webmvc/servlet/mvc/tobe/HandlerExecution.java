package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // TODO : 여러 형태의 HandlerExecution을 지원하면 좋을 듯
        return (ModelAndView) method.invoke(handler, request, response);
    }

    public Object getHandler() {
        return handler;
    }
}
