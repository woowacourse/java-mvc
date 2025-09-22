package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Object[] basePackage;
    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
        controllers = new HashMap<>();
    }

    public void initialize() {
        for (Object pkg : basePackage) {
            Reflections reflections = new Reflections(pkg.toString());
            controllers.putAll(instantiateControllers(
                    reflections.getTypesAnnotatedWith(Controller.class)
            ));
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        return controllers.stream()
                .collect(Collectors.toMap(
                        clazz -> clazz,
                        clazz -> getInstance(clazz)
                ));
    }

    private static Object getInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("controller 생성 실패");
        }
    }
}
