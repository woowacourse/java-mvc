package com.interface21.webmvc.servlet.mvc.tobe.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class InstanceManager {

    private static final InstanceManager instance = new InstanceManager();

    private final Map<Class<?>, Object> controllers = new HashMap<>();

    private InstanceManager() {
    }

    public static InstanceManager getInstance() {
        return instance;
    }

    public Object get(Class<?> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (controllers.containsKey(clazz)) {
            return controllers.get(clazz);
        }

        return create(clazz);
    }

    private Object create(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        Constructor<?> constructor = clazz.getConstructor();
        Object controllerInstance = constructor.newInstance();

        controllers.put(clazz, controllerInstance);
        return controllerInstance;
    }
}
