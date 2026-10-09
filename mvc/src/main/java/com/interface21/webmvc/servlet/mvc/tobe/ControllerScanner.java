package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Set<Class<?>> controllerClasses;

    public ControllerScanner(final Object... basePackage) {
        if (basePackage.length == 0) {
            this.controllerClasses = Set.of();
            return;
        }
        this.controllerClasses = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
    }

    public Map<Class<?>, Object> scan() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, createController(controllerClass));
        }
        return controllers;
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "인스턴스 생성 실패 @Controller class: " + controllerClass.getName(), e
            );
        }
    }
}
