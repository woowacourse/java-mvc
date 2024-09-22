package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::scanClass);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void scanClass(Class<?> c) {
        for (Method method : c.getDeclaredMethods()) {
            registerRequestMappingHandlerExecution(c, method);
        }
    }

    private void registerRequestMappingHandlerExecution(Class<?> c, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);

        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.add(
                    new HandlerKey(url, requestMethod), new HandlerExecution(c, method)
            );
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    public HandlerExecution findHandler(HttpServletRequest request) {
        return handlerExecutions.findHandler(request)
                .orElseThrow(() -> new IllegalArgumentException("No handler found for request: " + request.getRequestURI()));
    }
}
