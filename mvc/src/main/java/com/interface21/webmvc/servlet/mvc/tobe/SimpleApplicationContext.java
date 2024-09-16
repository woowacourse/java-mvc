package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleApplicationContext implements ApplicationContext {

    private static final SimpleApplicationContext INSTANCE = new SimpleApplicationContext();

    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    private SimpleApplicationContext() {
    }

    public static SimpleApplicationContext getInstance() {
        return INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        if (beans.containsKey(clazz)) {
            return (T) beans.get(clazz);
        }
        return createBean(clazz);
    }

    private <T> T createBean(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            T bean = constructor.newInstance();
            beans.put(clazz, bean);

            return bean;
        } catch (InstantiationException e) {
            throw new IllegalStateException("Cannot instantiate bean of type: " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access constructor of bean type: " + clazz.getName(), e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Constructor of bean type " + clazz.getName() + " threw an exception: " + e.getCause(), e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No default constructor for bean type: " + clazz.getName(), e);
        }
    }
}
