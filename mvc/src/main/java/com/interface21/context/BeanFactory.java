package com.interface21.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    private static BeanFactory INSTANCE;

    private final Map<Class<?>, Object> beans;

    private BeanFactory() {
        this.beans = new ConcurrentHashMap<>();
    }

    public static BeanFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeanFactory();
        }
        return INSTANCE;
    }

    public Object getBean(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        return registerBean(clazz);
    }

    private Object registerBean(Class<?> clazz) {
        try {
            Object bean = clazz.getDeclaredConstructor().newInstance();
            beans.put(clazz, bean);
            return bean;
        } catch (Throwable ex) {
            throw new BeanCreationException(clazz.getName(), ex);
        }
    }
}
