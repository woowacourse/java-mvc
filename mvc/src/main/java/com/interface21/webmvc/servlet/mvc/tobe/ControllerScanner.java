package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final String[] basePackages;

    public ControllerScanner(final String... basePackages) {
        this.basePackages = basePackages == null ? new String[0] : basePackages.clone();
    }

    public Map<Class<?>, Object> scan() {
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (final String basePackage : basePackages) {
            if (basePackage == null || basePackage.isBlank()) {
                continue;
            }

            final Reflections reflections = new Reflections(basePackage);
            final Set<Class<?>> candidates = reflections.getTypesAnnotatedWith(Controller.class);

            for (final Class<?> candidate : candidates) {
                controllers.computeIfAbsent(candidate, this::instantiateController);
            }
        }

        log.debug("Scanned {} controller(s) from packages {}", controllers.size(), String.join(", ", basePackages));
        return controllers;
    }

    private Object instantiateController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to instantiate controller: " + controllerClass.getName(), exception);
        }
    }
}
