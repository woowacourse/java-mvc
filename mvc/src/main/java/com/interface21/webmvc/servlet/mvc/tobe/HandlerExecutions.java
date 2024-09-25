package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HandlerExecutions {

    private static final Class<RequestMapping> HANDLER_ANNOTATION = RequestMapping.class;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

    public void setHandlerExecutions(Object controller) {
        Arrays.stream(controller.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(HANDLER_ANNOTATION))
                .toList()
                .forEach(this::setHandlerExecutions);
    }

    private void setHandlerExecutions(Method method) {
        RequestMapping requestMapping = method.getAnnotation(HANDLER_ANNOTATION);
        String url = requestMapping.value();
        RequestMethod[] methods = requestMapping.method();
        HandlerExecution handlerExecution = new HandlerExecution(method);
        for (RequestMethod requestMethod : methods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public HandlerExecution findByUrlAndMethod(String url, RequestMethod method) {
        HandlerKey handlerKey = new HandlerKey(url, method);
        return handlerExecutions.get(handlerKey);
    }
}
