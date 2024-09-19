package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.exception.UncheckedReflectiveOperationException;

public class InstancePool {

    private static final InstancePool INSTANCE = new InstancePool();

    private final Map<String, Object> instancePool = new HashMap<>();

    private InstancePool() {
    }

    public static InstancePool getSingleton() {
        return INSTANCE;
    }

    public Object getInstance(Class<?> clazz) {
        String instanceKey = toKey(clazz);
        if (instancePool.containsKey(instanceKey)) {
            return instancePool.get(instanceKey);
        }
        Object instance = createNewInstance(clazz);
        instancePool.put(instanceKey, instance);
        return instance;
    }

    private Object createNewInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                 InvocationTargetException |
                 IllegalAccessException |
                 NoSuchMethodException e) {
            throw new UncheckedReflectiveOperationException(e);
        }
    }

    private String toKey(Class<?> clazz) {
        return clazz.getName();
    }
}
