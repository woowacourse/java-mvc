package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.interface21.context.stereotype.Controller;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackages) {
        reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> scan() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            Constructor<?> constructor = getConstructor(controllerClass);
            Object controller = instantiate(constructor);
            controllers.put(controllerClass, controller);
        }
        return controllers;
    }

    private Constructor<?> getConstructor(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Constructor not found for class " + controllerClass.getName());
        }
    }

    private Object instantiate(final Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(
                    "Failed to instantiate class " + constructor.getDeclaringClass().getName());
        }
    }
}
