package com.interface21.webmvc.servlet.mvc.method;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final String basePackage;

    public ControllerScanner(final String basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> scanAndCreateControllers() {
        final Set<Class<?>> controllerClasses = findControllerClasses();
        return createControllerInstances(controllerClasses);
    }

    private Set<Class<?>> findControllerClasses() {
        log.info("Scanning for @Controller classes in package: {}", basePackage);
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        log.info("Found {} @Controller classes", controllers.size());
        controllers.forEach(clazz -> log.debug("Found controller: {}", clazz.getName()));

        return controllers;
    }

    private Map<Class<?>, Object> createControllerInstances(final Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllerInstances = new HashMap<>();

        for (final Class<?> controllerClass : controllerClasses) {
            try {
                final Object instance = createControllerInstance(controllerClass);
                controllerInstances.put(controllerClass, instance);
                log.debug("Created instance for controller: {}", controllerClass.getName());
            } catch (final Exception e) {
                log.error("Failed to create instance for controller: {}", controllerClass.getName(), e);
            }
        }

        return controllerInstances;
    }

    private Object createControllerInstance(final Class<?> controllerClass) throws Exception {
        return controllerClass.getDeclaredConstructor().newInstance();
    }
}
