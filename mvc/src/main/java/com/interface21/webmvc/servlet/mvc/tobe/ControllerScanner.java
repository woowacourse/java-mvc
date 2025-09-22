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

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers) {
        final Map<Class<?>, Object> instantiatedControllers = new HashMap<>();
        for (Class<?> controller : controllers) {
            try {
                instantiatedControllers.put(controller, controller.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                log.error("Failed to instantiate controller: {}", controller, e);
            }
        }
        return instantiatedControllers;
    }
}
