package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ControllerScanner {

    private final Object[] basePackages;

    public ControllerScanner(final Object... basePackages) {
        this.basePackages = basePackages;
    }

    public Map<Class<?>, Object> scan() {
        final var reflections = new Reflections(basePackages);
        final var controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, createController(controllerClass));
        }

        return controllers;
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return ReflectionUtils.accessibleConstructor(controllerClass).newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create controller: " + controllerClass.getName(), e);
        }
    }
}
