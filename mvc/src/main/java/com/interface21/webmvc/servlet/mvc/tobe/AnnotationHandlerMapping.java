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
        final var reflections = new Reflections(basePackage);

        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::registerController);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(final Class<?> controllerClass) {
        final var controller = createController(controllerClass);

        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandler(controller, method);
            }
        }
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create controller: " + controllerClass.getName(), e);
        }
    }

    private void registerHandler(final Object controller, final Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);

        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final var handlerExecution = new HandlerExecution(controller, method);

            if (!registerIfAbsent(handlerKey, handlerExecution)) {
                throw new IllegalStateException("Duplicate handler mapping: " + handlerKey);
            }
        }
    }

    private boolean registerIfAbsent(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        return handlerExecutions.putIfAbsent(handlerKey, handlerExecution) == null;
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        final var requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
