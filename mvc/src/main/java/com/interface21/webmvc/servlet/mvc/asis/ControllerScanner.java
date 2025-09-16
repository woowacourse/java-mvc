package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClassesWithAnnotation = reflections.getTypesAnnotatedWith(
                com.interface21.context.stereotype.Controller.class);

        return instantiateControllers(controllerClassesWithAnnotation);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> clazz : controllerClasses) {
            Object controllerInstance = createControllerInstance(clazz);
            controllers.put(clazz, controllerInstance);
        }
        return controllers;
    }

    private Object createControllerInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}