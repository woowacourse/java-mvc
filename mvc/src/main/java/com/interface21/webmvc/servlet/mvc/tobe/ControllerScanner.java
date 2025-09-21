package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllersClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllersClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllersClasses) {
        return controllersClasses.stream()
                .collect(Collectors.toMap(
                        controllerClass -> controllerClass,
                        this::getInstanceBy
                ));
    }

    private Object getInstanceBy(final Class<?> controllersClass) {
        try {
            return controllersClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
