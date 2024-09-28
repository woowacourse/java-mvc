package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
		this.basePackage = basePackage;
        this.handlerExecutions = ControllerScanner.scan(basePackage);

    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerExecution handlerExecution = handlerExecutions.get(
            new HandlerKey(request.getRequestURI(), RequestMethod.find(request.getMethod())));
        if(handlerExecution == null) {
            throw new IllegalArgumentException("No handler found for " + request.getRequestURI());
        }
        return handlerExecution;
    }
}
