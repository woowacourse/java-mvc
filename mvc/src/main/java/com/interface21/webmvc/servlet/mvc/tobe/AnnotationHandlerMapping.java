package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        for(Class<?> classes : reflections.getTypesAnnotatedWith(Controller.class)) {
            Object controller;

            try {
                controller = ReflectionUtils.accessibleConstructor(classes)
                        .newInstance();

                for (var method : classes.getDeclaredMethods()) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                    if(requestMapping == null) {
                        continue;
                    }

                    RequestMethod[] requestMethods = requestMapping.method();

                    if(requestMethods.length == 0) {
                        requestMethods = RequestMethod.values();
                    }

                    for(RequestMethod requestMethod : requestMethods) {
                        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

                        handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
                    }
                }
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
