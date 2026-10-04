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
        final Reflections reflections = new Reflections(basePackage);

        for (final Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            final Object controller = createControllerInstance(controllerClass);
            registerRequestMappings(controllerClass, controller);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerRequestMappings(final Class<?> controllerClass, final Object controller) {
        for (final Method method : controllerClass.getDeclaredMethods()) {
            final RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            if (mapping == null) {
                continue;
            }
            addMapping(method, mapping, controller);
        }
    }

    private static Object createControllerInstance(final Class<?> controllerClass) {
        final Object controller;
        try {
            controller = controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot instantiate controller: " + controllerClass, e);
        }
        return controller;
    }

    private void addMapping(final Method method, final RequestMapping mapping, final Object controller) {
        final String uri = mapping.value();
        final RequestMethod[] requestMethods = resolveRequestMethods(mapping);
        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private static RequestMethod[] resolveRequestMethods(final RequestMapping mapping) {
        final RequestMethod[] requestMethods = mapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
