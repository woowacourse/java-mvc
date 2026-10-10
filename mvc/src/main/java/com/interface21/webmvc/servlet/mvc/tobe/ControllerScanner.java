package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> getControllers() {
        final Map<Class<?>, Object> controllers = new LinkedHashMap<>();
        for (final Object packageName : basePackage) {
            final Reflections reflections = new Reflections(String.valueOf(packageName));
            final Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

            for (final Class<?> controllerType : controllerTypes) {
                controllers.putIfAbsent(controllerType, createController(controllerType));
            }
        }
        return controllers;
    }

    private Object createController(final Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (final ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not create controller: " + controllerType.getName(), exception);
        }
    }
}
