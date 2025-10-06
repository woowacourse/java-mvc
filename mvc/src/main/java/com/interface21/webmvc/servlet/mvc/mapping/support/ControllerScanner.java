package com.interface21.webmvc.servlet.mvc.mapping.support;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerTypes);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerTypes) {
        Map<Class<?>, Object> result = new HashMap<>();
        for (Class<?> controllerType : controllerTypes) {
            try {
                final Object instance = controllerType.getDeclaredConstructor().newInstance();
                result.put(controllerType, instance);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return Collections.unmodifiableMap(result);
    }
}
