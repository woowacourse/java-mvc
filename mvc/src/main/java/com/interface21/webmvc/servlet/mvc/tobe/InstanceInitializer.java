package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.core.util.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstanceInitializer {

    private final Map<Class<?>, Object> beanCache;

    public InstanceInitializer() {
        this.beanCache = new HashMap<>();
    }

    public Object createInstance(Class<?> clazz) throws ReflectiveOperationException {
        if (beanCache.containsKey(clazz)) {
            return beanCache.get(clazz);
        }

        Constructor<?> constructor = findConstructor(clazz);
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> dependencies = new ArrayList<>();

        for (Class<?> parameterType : parameterTypes) {
            dependencies.add(createInstance(parameterType));
        }

        ReflectionUtils.makeAccessible(constructor);
        Object instance = constructor.newInstance(dependencies.toArray());

        beanCache.put(clazz, instance);
        return instance;
    }

    private Constructor<?> findConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        return constructors[0];
    }
}
