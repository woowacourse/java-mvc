package com.interface21.webmvc.servlet.mvc;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(final String... basePackage) {
        Reflections reflections = new Reflections((Object[]) basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            registerController(controllerClass);
        }
    }

    private void registerController(Class<?> controllerClass) {
        try {
            Object controller = controllerClass.getDeclaredConstructor().newInstance();
            controllers.put(controllerClass, controller);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성에 실패했습니다: " + controllerClass.getSimpleName(), e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return Map.copyOf(controllers);
    }
}
