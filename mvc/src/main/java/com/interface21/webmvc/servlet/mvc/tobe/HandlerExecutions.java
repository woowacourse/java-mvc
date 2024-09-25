package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerExecutions {
    private final Map<HandlerKey, HandlerExecution> value = new HashMap<>();
    private final Map<String, Object> controllerInstanceCache = new HashMap<>();

    public HandlerExecution get(HandlerKey handlerKey) {
        return value.get(handlerKey);
    }

    public void put(String url, RequestMethod requestMethod, Class<?> controllerClazz, Method method) {
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        HandlerExecution handlerExecution = createHandlerExecution(controllerClazz, method);
        value.put(handlerKey, handlerExecution);
    }

    private HandlerExecution createHandlerExecution(Class<?> controllerClazz, Method method) {
        doCache(controllerClazz);
        return new HandlerExecution(controllerInstanceCache.get(controllerClazz.getName()), method);
    }

    private void doCache(Class<?> controllerClazz) {
        if (controllerInstanceCache.containsKey(controllerClazz.getName())) {
            return;
        }

        try {
            controllerInstanceCache.put(controllerClazz.getName(), controllerClazz.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
