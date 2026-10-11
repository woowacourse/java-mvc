package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> getControllers() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        final var controllerClasses = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
        for (final var controllerClass : controllerClasses) {
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
