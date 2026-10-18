package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllers = new HashMap<>();
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            try {
                Object controller = controllerClass.getDeclaredConstructor().newInstance();
                controllers.put(controllerClass, controller);
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException("Controller 생성 실패: " + controllerClass.getName(), e);
            }
        }
        return controllers;
    }
}
