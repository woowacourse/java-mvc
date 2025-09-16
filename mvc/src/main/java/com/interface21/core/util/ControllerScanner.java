package com.interface21.core.util;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    public static Map<Class<?>, Object> scanControllers(final Object... basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controller : controllerTypes) {
            try {
                controllers.put(controller, controller.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new IllegalStateException(controller.getSimpleName() + " 인스턴스 생성 실패");
            }
        }

        return controllers;
    }
}
