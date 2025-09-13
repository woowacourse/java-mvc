package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            scanHandlerMappings();
            log.info("Initialized AnnotationHandlerMapping!");
            System.out.println("handlerExecutions = " + handlerExecutions);
        } catch (Exception e) {
            log.error("an error occurred while scanning handler mappings.", e);
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        final var requestMethod = RequestMethod.of(request.getMethod());
        final var handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void scanHandlerMappings() throws Exception {
        final var reflections = new Reflections(basePackage);
        final var handlerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (final var clazz : handlerClasses) {
            scanHandlerMethods(clazz);
        }
    }

    private void scanHandlerMethods(final Class<?> handlerClasses) throws Exception {
        for (final var method : handlerClasses.getMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            final Object handler = handlerClasses.getDeclaredConstructor().newInstance();
            addHandlerMapping(handler, method);
        }
    }

    private void addHandlerMapping(final Object handler, final Method method) throws Exception {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        for (final var httpMethod : requestMapping.method()) {
            final var handlerKey = new HandlerKey(requestMapping.value(), httpMethod);
            final var handlerExecution = new HandlerExecution(handler, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
