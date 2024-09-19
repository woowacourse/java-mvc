package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
    private static final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

    private BeanFactory() {
    }

    public static synchronized Object getInstance(Class<?> clazz) throws ReflectiveOperationException {
        if (instances.containsKey(clazz)) {
            return instances.get(clazz);
        }
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        Object instance = constructor.newInstance();
        return instances.put(clazz, instance);
    }
}
