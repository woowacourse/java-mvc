package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackages) {
        log.info("basePackages : {}", basePackages);
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> scan() {
        final var lookup = new HashMap<Class<?>, Object>();
        final var controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final var controller : controllers) {
            final var controllerInstance = getInstanceBy(controller);
            lookup.put(controller, controllerInstance);
        }
        return lookup;
    }

    private Object getInstanceBy(final Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create controller instance: " + aClass.getName(), e);
        }
    }
}
