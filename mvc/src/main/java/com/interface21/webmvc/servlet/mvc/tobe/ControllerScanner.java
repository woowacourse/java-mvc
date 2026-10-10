package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> scan() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        final var reflections = new Reflections(basePackage);
        for (final Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            controllers.put(controllerClass, createController(controllerClass));
        }
        return controllers;
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot create controller: " + controllerClass.getName(), e);
        }
    }
}
