package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        try {
            Map<Class<?>, Object> controllers = controllerScanner.instantiateControllers();
            handlerExecutions.putAll(controllerScanner.scanHandlerExecutions(controllers));

            log.info("Handler mappings initialized: {} handlers registered", handlerExecutions.size());
        } catch (final Exception e) {
            log.error("Failed to initialize handler", e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        log.debug("Looking for handler for: {} {}", request.getMethod(), request.getRequestURI());

        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }
}
