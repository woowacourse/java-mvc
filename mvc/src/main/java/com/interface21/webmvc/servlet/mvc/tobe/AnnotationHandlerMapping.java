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
        final var reflections = new Reflections(basePackage);
        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            final var controller = createController(controllerClass);
            for (Method method : controllerClass.getDeclaredMethods()) {
                registerHandler(controller, method);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot instantiate controller: " + controllerClass.getName(), e);
        }
    }

    private void registerHandler(final Object controller, final Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }

        final var execution = new HandlerExecution(controller, method);
        final var requestMethods = requestMapping.method().length == 0
                ? RequestMethod.values()
                : requestMapping.method();

        for (RequestMethod requestMethod : requestMethods) {
            final var key = new HandlerKey(requestMapping.value(), requestMethod);
            final var previous = handlerExecutions.putIfAbsent(key, execution);
            if (previous != null && previous != execution) {
                throw new IllegalStateException("Duplicate handler mapping: " + key);
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }
}
