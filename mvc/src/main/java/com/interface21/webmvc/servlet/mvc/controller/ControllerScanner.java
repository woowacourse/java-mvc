package com.interface21.webmvc.servlet.mvc.controller;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {
    private final Reflections reflections;

    public static ControllerScanner from(Object[] basePackage) {
        return new ControllerScanner(new Reflections(basePackage));
    }

    private ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : classes) {
            Object controller = getController(clazz);
            controllers.put(clazz, controller);
        }

        return controllers;
    }

    private Object getController(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성에 실패했습니다 : " + clazz.getName(), e);
        }
    }
}
