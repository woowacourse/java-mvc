package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : types) {
            Method[] handlers = clazz.getMethods();
            for (Method handler : handlers) {
                addHandlerExecutions(clazz, handler);
            }
        }
    }

    private void addHandlerExecutions(Class<?> clazz, Method handler) {
        RequestMapping mappingAnnotation = handler.getAnnotation(RequestMapping.class);
        if (mappingAnnotation == null) {
            return;
        }
        for (RequestMethod requestMethod : mappingAnnotation.method()) {
            HandlerKey handlerKey = new HandlerKey(mappingAnnotation.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(clazz, handler));
        }
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        String requestURI = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
