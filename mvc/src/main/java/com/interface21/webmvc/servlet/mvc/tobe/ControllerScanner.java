package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    private ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public static ControllerScanner from(final Object[] basePackages) {
        return new ControllerScanner(new Reflections(basePackages));
    }

    public Map<Class<?>, Object> getControllers() throws Exception {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, controllerClass.getDeclaredConstructor().newInstance());
        }

        // Map<Controller 클래스, Controller 인스턴스> 반환
        return controllers;
    }
}
