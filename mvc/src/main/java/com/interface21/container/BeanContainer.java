package com.interface21.container;

import com.interface21.core.util.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanContainer {

    private final Map<String, Object> container = new ConcurrentHashMap<>();

    private BeanContainer() {
    }

    public static BeanContainer getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public <T> Object getBean(Class<T> clazz) throws Exception {
        String name = clazz.getName();
        if (container.containsKey(name)) {
            return container.get(name);
        }

        Constructor<T> constructor = ReflectionUtils.accessibleConstructor(clazz);
        Object bean = constructor.newInstance();

        container.put(name, bean);
        return bean;
    }

    private static class SingletonHelper {
        private static final BeanContainer INSTANCE = new BeanContainer();
    }

}
