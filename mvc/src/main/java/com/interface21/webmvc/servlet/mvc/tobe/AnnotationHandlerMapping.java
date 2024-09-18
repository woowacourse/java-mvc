package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        AnnotatedControllers controllers = AnnotatedControllers.from(basePackage);
        List<Handler> handlers = controllers.createHandlers();

        for (Handler handler : handlers) {
            HandlerKey handlerKey = handler.key();
            validateHandlerKey(handlerKey);
            HandlerExecution handlerExecution = handler.execution();
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Initialized AnnotationHandlerMapping: {} {}", handlerKey, handlerExecution);
        }
    }

    private void validateHandlerKey(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            log.error("HandlerKey collision detected for: {}. This key is already in use.", handlerKey);
            throw new IllegalArgumentException("Duplicate handlerExecution mappings are not allowed.");
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }

        log.error("No handler found for the request: {}", request);
        throw new IllegalArgumentException("No handler found for the request");
    }
}
