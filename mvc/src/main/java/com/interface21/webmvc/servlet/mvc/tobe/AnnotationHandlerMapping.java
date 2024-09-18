package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllerClasses) {
            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String uri = requestMapping.value();
                        Arrays.stream(requestMapping.method())
                                .forEach(requestMethod -> {
                                    HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
                                    HandlerExecution handlerExecution = new HandlerExecution(method, clazz);
                                    handlerExecutions.put(handlerKey, handlerExecution);
                                });
                    });
        }

    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.find(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, method);
        return handlerExecutions.get(handlerKey);
    }
}
