package com.interface21.webmvc.servlet.mvc;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Object[] basePackages;

    public ControllerScanner(final Object... basePackages) {
        this.basePackages = basePackages;
    }

    public Map<Class<?>, Object> scan() {
        final var controllers = new HashMap<Class<?>, Object>();
        final var controllerClasses = findControllerClasses();

        for (final var clazz : controllerClasses) {
            final var instance = instantiateController(clazz);
            controllers.put(clazz, instance);
        }

        return controllers;
    }

    private Set<Class<?>> findControllerClasses() {
        final var reflections = new Reflections(basePackages);

        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Object instantiateController(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor()
                    .newInstance();
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to instantiate controller: " + clazz.getName(), e);
        }
    }
}
