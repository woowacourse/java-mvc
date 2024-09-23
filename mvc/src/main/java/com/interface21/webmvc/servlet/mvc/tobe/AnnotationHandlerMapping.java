package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, Handler> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        HandlerScanner.scanHandlers(basePackage).forEach(this::registerHandler);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandler(Handler handler) {
        String value = handler.getUrl();
        RequestMethod[] requestMethods = handler.getRequestMethods();

        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(value, requestMethod), handler);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), getRequestMethod(request));
        Handler handler = handlerExecutions.get(handlerKey);

        if (handler == null) {
            throw new IllegalArgumentException("핸들러가 존재하지 않습니다.");
        }

        return handler;
    }

    private RequestMethod getRequestMethod(HttpServletRequest request) {
        try {
            return RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("잘못된 HTTP 메서드입니다.");
        }
    }
}
