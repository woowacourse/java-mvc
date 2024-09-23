package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.core.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ClassManager {

    private static final ClassManager INSTANCE = new ClassManager();

    private final Map<Class<?>, Object> classPool;

    private ClassManager() {
        this.classPool = new HashMap<>();
    }

    public static ClassManager getInstance() {
        return INSTANCE;
    }

    public Object get(Class<?> clazz) {
        if (classPool.containsKey(clazz)) {
            return classPool.get(clazz);
        }
        try {
            return put(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Object put(Class<?> clazz) throws Exception {
        Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz);
        Object controller = constructor.newInstance();
        classPool.putIfAbsent(clazz, controller);
        return controller;
    }
}
