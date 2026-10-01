package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage.clone();
        this.handlerExecutions = Map.of();
    }

    public void initialize() {
        final var initializedHandlerExecutions = new HashMap<HandlerKey, HandlerExecution>();
        final var reflections = new Reflections(basePackage);

        for (final var controllerType : reflections.getTypesAnnotatedWith(Controller.class)) {
            final var controller = createController(controllerType);
            registerHandlerMethods(initializedHandlerExecutions, controller);
        }

        handlerExecutions = Map.copyOf(initializedHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestUri = request.getRequestURI();
        final var requestMethod = findRequestMethod(request.getMethod());

        if (requestMethod != null) {
            final var handlerExecution = handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
            if (handlerExecution != null) {
                return handlerExecution;
            }
        }

        return handlerExecutions.get(new HandlerKey(requestUri, null));
    }

    private Object createController(final Class<?> controllerType) {
        try {
            final Constructor<?> constructor = controllerType.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to create controller: " + controllerType.getName(), exception);
        }
    }

    private void registerHandlerMethods(final Map<HandlerKey, HandlerExecution> initializedHandlerExecutions,
                                        final Object controller) {
        for (final var method : controller.getClass().getDeclaredMethods()) {
            final var requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            registerHandlerExecution(initializedHandlerExecutions, controller, method, requestMapping);
        }
    }

    private void registerHandlerExecution(final Map<HandlerKey, HandlerExecution> initializedHandlerExecutions,
                                          final Object controller,
                                          final Method method,
                                          final RequestMapping requestMapping) {
        final var requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            register(initializedHandlerExecutions, new HandlerKey(requestMapping.value(), null), controller, method);
            return;
        }

        for (final var requestMethod : requestMethods) {
            register(initializedHandlerExecutions, new HandlerKey(requestMapping.value(), requestMethod), controller, method);
        }
    }

    private void register(final Map<HandlerKey, HandlerExecution> initializedHandlerExecutions,
                          final HandlerKey handlerKey,
                          final Object controller,
                          final Method method) {
        final var previousHandler = initializedHandlerExecutions.putIfAbsent(
                handlerKey,
                new HandlerExecution(controller, method)
        );
        if (previousHandler != null) {
            throw new IllegalStateException("Duplicate handler mapping: " + handlerKey);
        }
    }

    private RequestMethod findRequestMethod(final String method) {
        try {
            return RequestMethod.valueOf(method);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
