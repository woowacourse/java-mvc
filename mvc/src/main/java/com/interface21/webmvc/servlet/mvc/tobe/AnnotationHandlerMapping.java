package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
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
        final var controllerTypes = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
        for (final var controllerType : controllerTypes) {
            final Object controller;
            try {
                controller = ReflectionUtils.accessibleConstructor(controllerType).newInstance();
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Failed to create controller: " + controllerType.getName(), e);
            }

            for (final var method : controllerType.getMethods()) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                final var requestMapping = method.getAnnotation(RequestMapping.class);
                final var requestMethods = requestMapping.method().length == 0
                        ? RequestMethod.values() : requestMapping.method();
                final var handlerExecution = new HandlerExecution(controller, method);
                for (final var requestMethod : requestMethods) {
                    final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                    if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                        throw new IllegalStateException("Duplicate request mapping: " + handlerKey);
                    }
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
