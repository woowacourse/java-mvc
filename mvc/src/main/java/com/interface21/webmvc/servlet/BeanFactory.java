package com.interface21.webmvc.servlet;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
    private static final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

    private BeanFactory() {
    }

    public static Object getInstance(Class<?> clazz) {
        return instances.computeIfAbsent(clazz, key -> {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                return constructor.newInstance();
            } catch (ReflectiveOperationException e) {
                throw new UncheckedServletException(e);
            }
        });
    }
}
