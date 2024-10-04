package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;
    private final Map<Class<?>, Object> cachedInstances;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
        HashSet<Class<?>> controllers = new HashSet<>(reflections.getTypesAnnotatedWith(Controller.class));
        this.cachedInstances = initiateControllers(controllers);
    }

    public Map<Class<?>, Object> getControllers() {
        return Collections.unmodifiableMap(this.cachedInstances);
    }

    private Map<Class<?>, Object> initiateControllers(Set<Class<?>> clazz) {
        return clazz.stream().collect(Collectors.toMap(
                clazz1 -> clazz1,
                this::getInstance
        ));
    }

    private Object getInstance(Class<?> clazz) {
        try {
            return clazz.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
