package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> scan() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Class<?>, Object> controllers = new HashMap<>();

        controllerClasses.forEach(controllerClass ->
                controllers.put(controllerClass, createController(controllerClass))
        );
        return controllers;
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create controller: " + controllerClass.getName(), e);
        }
    }
}
