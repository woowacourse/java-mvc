package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonManager {

    private static final SingletonManager instance = new SingletonManager();

    private final Map<Class<?>, Object> singletonObjects = new ConcurrentHashMap<>();

    private SingletonManager() {
    }

    public static SingletonManager getInstance() {
        return instance;
    }

    public Object get(Class<?> clazz) {
        if (singletonObjects.containsKey(clazz)) {
            return singletonObjects.get(clazz);
        }
        return createSingleton(clazz);
    }

    private Object createSingleton(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            Object classInstance = constructor.newInstance();
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
}
