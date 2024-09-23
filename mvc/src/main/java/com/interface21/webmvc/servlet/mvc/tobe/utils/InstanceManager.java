package com.interface21.webmvc.servlet.mvc.tobe.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstanceManager {

    private static final InstanceManager instance = new InstanceManager();

    private InstanceManager() {
    }

    public static InstanceManager getInstance() {
        return instance;
    }

    public Object get(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        Constructor<?> constructor = clazz.getConstructor();
        return constructor.newInstance();
    }
}
