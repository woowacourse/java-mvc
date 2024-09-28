package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object instanceController;
    private final Method method;

    public HandlerExecution(Object instanceController, Method method) {
        this.instanceController = instanceController;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
            throws InvocationTargetException, IllegalAccessException {
        return (ModelAndView) method.invoke(instanceController, request, response);
    }

    public String getUrl() {
        RequestMapping requestMapping = getRequestMapping();
        return requestMapping.value();
    }

    public RequestMethod[] getRequestMethods() {
        RequestMapping requestMapping = getRequestMapping();
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    private RequestMapping getRequestMapping() {
        return method.getAnnotation(RequestMapping.class);
    }
}
