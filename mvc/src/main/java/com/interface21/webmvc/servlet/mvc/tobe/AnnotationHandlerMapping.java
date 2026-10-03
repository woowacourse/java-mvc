package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
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
        final var controllerClasses = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controllerClass : controllerClasses) {
            final var target = createController(controllerClass);
            for (final var method : controllerClass.getDeclaredMethods()) {
                final var mapping = method.getAnnotation(RequestMapping.class);
                if (mapping == null) {
                    continue;
                }

                final var handlerExecution = new HandlerExecution(target, method);
                final var requestMethods = mapping.method().length == 0
                        ? RequestMethod.values()
                        : mapping.method();
                for (final var requestMethod : requestMethods) {
                    final var handlerKey = new HandlerKey(mapping.value(), requestMethod);
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(
                request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create controller: " + controllerClass.getName(), e);
        }
    }
}
