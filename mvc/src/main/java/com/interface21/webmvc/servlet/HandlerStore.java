package com.interface21.webmvc.servlet;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerStore {

    private static final Map<String, Object> MANAGERS = new ConcurrentHashMap<>();

    public void registerHandler(List<Object> objects) {
        for (Object object : objects) {
            String clazzName = object.getClass().getName();
            if (MANAGERS.containsKey(clazzName)) {
                throw new IllegalArgumentException("이미 등록되어 있는 클래스입니다");
            }
            MANAGERS.put(clazzName, object);
        }
    }

    public <T> List<T> getHandler(Class<T> clazz) {
        return MANAGERS.values().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }

    public List<Object> getHandlerWithAnnotation(Class<? extends Annotation> annotation) {
        return MANAGERS.values().stream()
                .filter(clazz -> clazz.getClass().isAnnotationPresent(annotation))
                .toList();
    }
}
