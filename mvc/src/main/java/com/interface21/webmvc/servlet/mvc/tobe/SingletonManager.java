package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingletonManager {

    private static final Logger log = LoggerFactory.getLogger(SingletonManager.class);
    private static final SingletonManager instance = new SingletonManager();

    private final Map<Class<?>, Object> instanceContainer = new ConcurrentHashMap<>();

    private SingletonManager() {
    }

    public static SingletonManager getInstance() {
        return instance;
    }

    public Object get(Class<?> clazz) {
        if (instanceContainer.containsKey(clazz)) {
            return instanceContainer.get(clazz);
        }
        try {
            Constructor<?> constructor = clazz.getConstructor();
            Object classInstance = constructor.newInstance();
            instanceContainer.putIfAbsent(clazz, classInstance);
            return classInstance;
        } catch (InvocationTargetException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException e) {
            log.error("Failed to instantiate instance");
            throw new RuntimeException(e);
        }
    }
}
