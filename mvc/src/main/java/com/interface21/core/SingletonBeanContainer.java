package com.interface21.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonBeanContainer {

    private static final SingletonBeanContainer instance = new SingletonBeanContainer();

    private final Map<Class<?>, Object> singletonObjects = new ConcurrentHashMap<>();

    private SingletonBeanContainer() {
    }

    public static SingletonBeanContainer getInstance() {
        return instance;
    }

    public Object createSingleton(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            Object classInstance = constructor.newInstance();
            constructor.setAccessible(false);
            singletonObjects.putIfAbsent(clazz, classInstance);
            return classInstance;
        } catch (IllegalArgumentException e) {
            throw new SingletonInstantiationException(clazz, "Arguments mismatch", e);
        } catch (IllegalAccessException e) {
            throw new SingletonInstantiationException(clazz, "Inaccessible constructor", e);
        } catch (InstantiationException e) {
            throw new SingletonInstantiationException(clazz, "Failed to instantiate class", e);
        } catch (InvocationTargetException e) {
            throw new SingletonInstantiationException(clazz, "Exception occurred on target class", e);
        } catch (NoSuchMethodException e) {
            throw new SingletonInstantiationException(clazz, "No constructor found (interface, array class, ..)", e);
        }
    }

    public Object getTypedBean(Class<?> clazz) {
        if (singletonObjects.containsKey(clazz)) {
            return singletonObjects.get(clazz);
        }
        return createSingleton(clazz);
    }

    public List<Object> getAnnotatedBeans(Class<? extends Annotation> annotation) {
        return singletonObjects.values()
                .stream()
                .filter(obj -> obj.getClass().isAnnotationPresent(annotation))
                .toList();
    }
}
