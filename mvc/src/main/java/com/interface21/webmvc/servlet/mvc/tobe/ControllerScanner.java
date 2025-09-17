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

    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(final Object[] basePackages) {
        final Reflections reflections = new Reflections(basePackages);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        this.controllers = instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        try {
            for (Class<?> controllerClass : controllerClasses) {
                final Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                controllers.put(controllerClass, controllerInstance);
            }
            return controllers;
        } catch (final Exception e) {
            log.error("Error while instantiating controller", e);
            throw new RuntimeException(e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
