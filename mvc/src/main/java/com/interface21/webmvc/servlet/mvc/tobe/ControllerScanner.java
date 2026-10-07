package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ControllerScanner {

    public Map<Class<?>, Object> scan(final Object... basePackage) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        if (basePackage.length == 0) {
            return controllers;
        }

        final Reflections reflections = new Reflections(basePackage);
        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
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
