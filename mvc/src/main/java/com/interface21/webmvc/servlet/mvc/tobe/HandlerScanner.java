package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;


public class HandlerScanner {

    private final Reflections reflections;

    public HandlerScanner(Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getHandlers() {
        Set<Class<?>> handlerTypes = reflections.getTypesAnnotatedWith(Controller.class);

        Map<Class<?>, Object> handlers = new HashMap<>();
        for (Class<?> handlerType : handlerTypes) {
            Object handler = instantiateHandler(handlerType);
            handlers.put(handlerType, handler);
        }
        return handlers;
    }

    private Object instantiateHandler(Class<?> handlerType) {
        try {
            return handlerType.getDeclaredConstructor().newInstance();
        } catch (Throwable ex) {
            throw new HandlerCreationException(handlerType.getName(), ex);
        }
    }
}
