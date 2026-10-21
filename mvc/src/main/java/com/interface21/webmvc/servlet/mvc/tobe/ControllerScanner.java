package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> scan() {
        final Set<Class<?>> controllerTypes = new HashSet<>();
        for (Object base : basePackage) {
            final var reflections = new Reflections(base);
            controllerTypes.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        }

        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerType : controllerTypes) {
            final var controller = createController(controllerType);
            controllers.put(controllerType, controller);
        }
        return controllers;
    }

    private Object createController(final Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to instantiate controller: " + controllerType.getName(), e);
        }
    }
}
