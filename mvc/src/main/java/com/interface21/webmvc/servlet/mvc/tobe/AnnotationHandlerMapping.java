package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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
        new Reflections(basePackage)
                .getTypesAnnotatedWith(Controller.class)
                .forEach(this::registerHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }

    private void registerHandlerExecutions(final Class<?> controllerType) {
        final Object controller = instantiate(controllerType);

        for (final Method method : controllerType.getDeclaredMethods()) {
            final var requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }
            for (final var requestMethod : resolveRequestMethods(requestMapping)) {
                final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                final var handlerExecution = new HandlerExecution(controller, method);
                final var existingHandler = handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
                if (existingHandler != null) {
                    throw new IllegalStateException("Duplicate handler mapping: " + handlerKey
                            + " (" + existingHandler + ", " + handlerExecution + ")");
                }
            }
        }
    }

    private RequestMethod[] resolveRequestMethods(final RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    private Object instantiate(final Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot create controller: " + controllerType.getName(), e);
        }
    }
}
