package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final var controllerType : reflections.getTypesAnnotatedWith(Controller.class)) {
            try {
                final var controller = ReflectionUtils.accessibleConstructor(controllerType).newInstance();
                controllers.put(controllerType, controller);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Failed to create controller: " + controllerType.getName(), e);
            }
        }
        return controllers;
    }
}
