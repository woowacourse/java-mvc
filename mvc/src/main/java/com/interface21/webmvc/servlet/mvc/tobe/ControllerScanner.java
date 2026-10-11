package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, createController(controllerClass));
        }

        return controllers;
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            Constructor<?> constructor = controllerClass.getDeclaredConstructor();

            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 객체 생성에 실패했습니다: " + controllerClass.getName(), e);
        }
    }
}
