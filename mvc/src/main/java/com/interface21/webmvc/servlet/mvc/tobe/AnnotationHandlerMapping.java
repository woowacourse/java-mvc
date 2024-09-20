package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.put(new HandlerKey("/get-test", RequestMethod.GET), new HandlerExecution("/get-test.jsp"));
        handlerExecutions.put(new HandlerKey("/post-test", RequestMethod.POST), new HandlerExecution("/post-test.jsp"));
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKey(request);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new IllegalArgumentException("해당 요청에 대응하는 핸들러가 없습니다.");
        }
        return handlerExecution;
    }

    private HandlerKey createHandlerKey(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.find(request.getMethod());
        return new HandlerKey(requestURI, requestMethod);
    }
}
