package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method handler) {
        this.method = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Constructor<?> noArgsConstructor = method.getDeclaringClass().getConstructor();
        Object controller = noArgsConstructor.newInstance();
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
