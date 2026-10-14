package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final HandlerMethodResolver handlerMethodResolver = new HandlerMethodResolver();
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Map.of();

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        final Map<HandlerKey, HandlerExecution> mappings = new HashMap<>();
        for (final var entry : controllerScanner.scan().entrySet()) {
            final Class<?> controllerClass = entry.getKey();
            for (final Method method : handlerMethodResolver.findHandlerMethods(controllerClass)) {
                final var handler = new HandlerExecution(entry.getValue(), method);
                final var mapping = handlerMethodResolver.findRequestMapping(controllerClass, method);
                for (final RequestMethod requestMethod : handlerMethodResolver.resolveRequestMethods(mapping)) {
                    final var key = new HandlerKey(mapping.value(), requestMethod);
                    if (mappings.putIfAbsent(key, handler) != null) {
                        throw new IllegalStateException("Duplicate request mapping: " + key);
                    }
                    log.info("Mapped {} to {}.{}", key, controllerClass.getSimpleName(), method.getName());
                }
            }
        }
        handlerExecutions = Map.copyOf(mappings);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        final var requestPath = request.getRequestURI().substring(request.getContextPath().length());
        return handlerExecutions.get(new HandlerKey(requestPath, requestMethod));
    }
}
