package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.interface21.web.bind.annotation.RequestMethod;

public class HandlerMappings {
    private final Map<HandlerKey, HandlerExecution> handlerMappings;
    private final Map<Class<?>, Object> handlerInstances;

    public HandlerMappings() {
        this.handlerMappings = new HashMap<>();
        this.handlerInstances = new HashMap<>();
    }

    public void addHandler(Method method, String uri, RequestMethod requestMethod) {
        Object handlerInstance = createHandlerInstance(method.getDeclaringClass());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        validateHandlerKey(handlerKey);
        HandlerExecution handlerExecution = new HandlerExecution(method, handlerInstance);
        handlerMappings.put(handlerKey, handlerExecution);
    }

    private Object createHandlerInstance(Class<?> clazz) {
        if (handlerInstances.containsKey(clazz)) {
            return handlerInstances.get(clazz);
        }
        try {
            handlerInstances.put(clazz, clazz.getConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance for class: " + clazz.getName());
        }
        return handlerInstances.get(clazz);
    }

    private void validateHandlerKey(HandlerKey handlerKey) {
        if (handlerMappings.containsKey(handlerKey)) {
            throw new IllegalArgumentException("HandlerExecution mappings already exists with handler key: " + handlerKey.toString());
        }
    }

    public Object getHandler(HandlerKey handlerKey) {
        return handlerMappings.get(handlerKey);
    }
}
