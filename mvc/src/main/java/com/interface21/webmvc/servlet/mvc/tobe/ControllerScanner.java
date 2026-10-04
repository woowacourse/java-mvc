package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final String[] basePackages;

    public ControllerScanner(final String... basePackages) {
        if (basePackages == null || basePackages.length == 0) {
            throw new IllegalArgumentException("At least one base package is required");
        }
        this.basePackages = basePackages.clone();
    }

    public Map<Class<?>, Object> scan() {
        final Reflections reflections = new Reflections((Object[]) basePackages);
        final Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Class<?>, Object> controllers = new LinkedHashMap<>();

        for (final Class<?> controllerType : controllerTypes) {
            try {
                final Constructor<?> constructor = ReflectionUtils.accessibleConstructor(controllerType);
                controllers.put(controllerType, constructor.newInstance());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(
                        "Could not create controller instance: " + controllerType.getName(), e);
            }
        }

        return controllers;
    }
}
