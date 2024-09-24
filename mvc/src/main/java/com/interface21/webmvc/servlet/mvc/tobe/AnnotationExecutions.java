package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationExecutions {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationExecutions() {
        this.handlerExecutions = new HashMap<>();
    }

    public HandlerExecution getHandler(String uri, RequestMethod requestMethod) {
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    public boolean hasHandler(String uri, RequestMethod requestMethod) {
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        return handlerExecutions.containsKey(handlerKey);
    }

    public void addExecutor(Object instance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            log.error("RequestMapping 메서드가 아닙니다. {}", method.getName());
            return;
        }
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods == null || requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(instance, method));
        }
    }
}
