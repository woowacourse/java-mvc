package com.interface21.webmvc.servlet.mvc.tobe;

import static com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner.*;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initializing AnnotationHandlerMapping...");
        handlerExecutions.putAll(scan(basePackage));
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerExecution handlerExecution = handlerExecutions.get(
            new HandlerKey(request.getRequestURI(), RequestMethod.find(request.getMethod())));
        if (handlerExecution == null) {
            throw new IllegalArgumentException("No handler found for " + request.getRequestURI());
        }
        return handlerExecution;
    }
}
