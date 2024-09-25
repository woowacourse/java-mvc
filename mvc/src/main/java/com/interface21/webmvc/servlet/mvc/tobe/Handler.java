package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class Handler {

    private static final Class<RequestMapping> REQUEST_MAPPING_ANNOTATION = RequestMapping.class;

    private final Method method;
    private final Object instance;
    private RequestMapping requestMapping;

    public Handler(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }

    public RequestMethod[] getRequestMethods() {
        if (requestMapping == null) {
            requestMapping = getRequestMapping();
        }
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    public String getUri() {
        if (requestMapping == null) {
            requestMapping = getRequestMapping();
        }
        return requestMapping.value();
    }

    private RequestMapping getRequestMapping() {
        return method.getDeclaredAnnotation(REQUEST_MAPPING_ANNOTATION);
    }
}
