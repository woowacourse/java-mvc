package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> controllerClass : controllerClasses) {
            try {
                final Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                controllers.put(controllerClass, controllerInstance);
            } catch (Exception e) {
                throw new RuntimeException("컨트롤러 인스턴스 생성 실패: " + controllerClass.getName(), e);
            }
        }
        return controllers;
    }
}
