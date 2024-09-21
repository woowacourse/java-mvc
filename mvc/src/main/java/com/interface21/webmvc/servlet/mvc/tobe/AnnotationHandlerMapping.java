package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final AnnotationHandlerFinder handlerFinder;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        this.handlerFinder = new AnnotationHandlerFinder(basePackage);
    }

    public void initialize() {
        List<Handler> handlers = handlerFinder.findHandlers();
        handlers.forEach(this::registerHandler);
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet().forEach(
            handlerKey -> log.info("Path : {}, Method : {}", handlerKey.getUrl(),
                handlerKey.getRequestMethod()));
    }

    private void registerHandler(Handler handler) {
        for (RequestMethod requestMethod : handler.getRequestMethods()) {
            HandlerKey handlerKey = new HandlerKey(handler.getUri(), requestMethod);
            validateUniqueHandler(handlerKey);
            handlerExecutions.put(handlerKey, new HandlerExecution(handler));
        }
    }

    private void validateUniqueHandler(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException(
                String.format("동일한 핸들러는 등록될 수 없습니다. URI: %s, Method: %s",
                    handlerKey.getUrl(), handlerKey.getRequestMethod()));
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(uri, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
