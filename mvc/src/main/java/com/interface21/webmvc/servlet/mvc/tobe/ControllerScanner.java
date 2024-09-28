package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        controllers = classes.stream()
                .collect(Collectors.toMap(clazz -> clazz, this::createInstance, (o1, o2) -> o1));
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return Collections.unmodifiableMap(controllers);
    }
}
