package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final AnnotationHandlerFinder handlerFinder;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.handlerFinder = new AnnotationHandlerFinder(basePackage);
    }

    public void initialize() {
        List<Handler> handlers = handlerFinder.findHandlers();
        handlers.forEach(this::registerHandler);
        log.info("Initialized AnnotationHandlerMapping!");
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
        HandlerKey handlerKey = createHandlerKey(request);
        return findHandler(handlerKey);
    }

    private HandlerKey createHandlerKey(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        return new HandlerKey(uri, RequestMethod.valueOf(method));
    }

    private HandlerExecution findHandler(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        throw new NoSuchElementException(String.format("핸들러를 찾을 수 없습니다. URI: %s, Method: %s",
            handlerKey.getUrl(), handlerKey.getRequestMethod()));
    }
}
