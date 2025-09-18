package com.interface21.webmvc.servlet.mvc;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    public Map<Class<?>, Object> scan(final Object[] basePackage) {
        try {
            return doScan(basePackage);
        } catch (final NoSuchMethodException | InvocationTargetException | InstantiationException |
                       IllegalAccessException e) {
            throw new IllegalStateException("Failed to scan controllers", e);
        }
    }

    private Map<Class<?>, Object> doScan(final Object[] basePackage)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClazzs = reflections.getTypesAnnotatedWith(Controller.class);

        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> controllerClazz : controllerClazzs) {
            controllers.put(controllerClazz, controllerClazz.getDeclaredConstructor().newInstance());
        }
        return controllers;
    }
}
