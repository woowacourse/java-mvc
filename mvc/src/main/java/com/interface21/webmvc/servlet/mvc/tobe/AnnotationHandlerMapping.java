package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ClassScanner classScanner;
    private final HandlerMappings handlerMappings;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.classScanner = new ClassScanner(basePackage);
        this.handlerMappings = new HandlerMappings();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        List<Method> methods = classScanner.findHandlingMethods();
        methods.forEach(this::addHandlerExecutions);
    }

    private void addHandlerExecutions(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        Arrays.stream(requestMethods)
                .forEach(requestMethod -> handlerMappings.addHandler(method, requestMapping.value(), requestMethod));
    }

    private static RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.find(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, method);

        return handlerMappings.getHandler(handlerKey);
    }
}
