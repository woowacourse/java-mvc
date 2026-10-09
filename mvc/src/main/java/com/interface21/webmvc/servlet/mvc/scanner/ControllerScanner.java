package com.interface21.webmvc.servlet.mvc.scanner;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    public static Map<Class<?>, Object> getControllers(final Object... basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private static Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            try {
                controllers.put(controllerClass, controllerClass.getDeclaredConstructor().newInstance());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Controller 초기화에 실패했습니다: " + controllerClass.getName(), e);
            }
        }

        return controllers;
    }
}
