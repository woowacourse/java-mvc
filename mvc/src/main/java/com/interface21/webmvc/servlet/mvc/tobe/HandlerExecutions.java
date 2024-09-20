package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HandlerExecutions {

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public HandlerExecutions() {
        this.handlerExecutions = new HashMap<>();
    }

    public void mappingHandler(Class<?> controller, Method method, RequestMapping requestMapping) {
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    public HandlerExecution get(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
