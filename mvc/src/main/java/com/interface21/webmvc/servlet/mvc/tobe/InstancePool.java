package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class InstancePool {

    private static final InstancePool INSTANCE = new InstancePool();

    private final Map<String, Object> instancePool = new HashMap<>();

    private InstancePool() {
    }

    public static InstancePool getSingleton() {
        return INSTANCE;
    }

    public Object getInstance(Class<?> clazz) {
        try {
            String instanceKey = toKey(clazz);
            if (instancePool.containsKey(instanceKey)) {
                return instancePool.get(instanceKey);
            }
            Object instance = clazz.getDeclaredConstructor().newInstance();
            instancePool.put(instanceKey, instance);
            return instance;
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private String toKey(Class<?> clazz) {
        return clazz.getName();
    }
}
