package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        handlerExecutions.clear();

        final Map<Class<?>, Object> controllers = new ControllerScanner(basePackage).scan();
        for (final Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            registerController(entry.getKey(), entry.getValue());
        }

        log.info("Initialized AnnotationHandlerMapping with {} handlers!", handlerExecutions.size());
    }

    private void registerController(final Class<?> controllerClass, final Object controller) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            final RequestMethod[] requestMethods = requestMapping.method().length == 0
                    ? RequestMethod.values()
                    : requestMapping.method();
            for (RequestMethod requestMethod : requestMethods) {
                final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                final HandlerExecution existingHandler = handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
                if (existingHandler != null) {
                    throw new IllegalStateException("Duplicate handler mapping for " + handlerKey
                            + " in " + controllerClass.getName() + "#" + method.getName());
                }
                log.info("Mapped {} {} to {}.{}", requestMethod, requestMapping.value(),
                        controllerClass.getSimpleName(), method.getName());
            }
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }

        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
