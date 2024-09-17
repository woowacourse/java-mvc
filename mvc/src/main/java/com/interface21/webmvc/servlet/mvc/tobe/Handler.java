package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class Handler {

    private final Method handler;
    private final Object instance;

    public Handler(Method handler, Object instance) {
        this.handler = handler;
        this.instance = instance;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {
        return (ModelAndView) handler.invoke(instance, request, response);
    }

    public RequestMethod[] getRequestMethods() {
        RequestMapping requestMapping = handler.getDeclaredAnnotation(RequestMapping.class);
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    public String getUri() {
        RequestMapping requestMapping = handler.getDeclaredAnnotation(RequestMapping.class);
        return requestMapping.value();
    }
}
