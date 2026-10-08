package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {
    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage.clone();
    }

    public Map<Class<?>, Object> scan() {
        final var reflections = new Reflections(basePackage);

        final Map<Class<?>, Object> controllers =
                reflections.getTypesAnnotatedWith(Controller.class)
                        .stream()
                        .collect(Collectors.toUnmodifiableMap(
                                controllerType -> controllerType,
                                this::createController
                        ));

        return controllers;
    }

    private Object createController(final Class<?> controllerType) {
        try {
            final Constructor<?> constructor =
                    controllerType.getDeclaredConstructor();

            return constructor.newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(
                    "Failed to create controller: " + controllerType.getName(),
                    exception
            );
        }
    }
}
