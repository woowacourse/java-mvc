package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        int parameterCount = method.getParameterCount();
        if (parameterCount == 2 && parameterTypes[0] == HttpServletRequest.class && parameterTypes[1] == HttpServletResponse.class) {
            return (ModelAndView) method.invoke(handler, request, response);
        } else if (parameterCount == 1 && parameterTypes[0] == HttpServletRequest.class) {
            return (ModelAndView) method.invoke(handler, request);
        } else if (parameterCount == 0) {
            return (ModelAndView) method.invoke(handler);
        }
        throw new IllegalStateException("Handler ParameterTypes Not Supported : " + Arrays.toString(parameterTypes));
    }

    public Object getHandler() {
        return handler;
    }
}
