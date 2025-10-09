package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final String[] basePackage;

    public ControllerScanner(final String... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> scan() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        final var reflections = new Reflections((Object) basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controllerClass : controllerClasses) {
            try {
                final Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                controllers.put(controllerClass, controllerInstance);
            } catch (Exception e) {
                log.error("Failed to create instance of controller: {}", controllerClass.getName(), e);
            }
        }
        return controllers;
    }
}
