package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiateController(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateController(final Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            try {
                Object controller = controllerClass.getDeclaredConstructor().newInstance();
                controllers.put(controllerClass, controller);
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException("컨트롤러 생성 실패: " + controllerClass.getName(), e);
            }
        }

        return controllers;
    }
}
