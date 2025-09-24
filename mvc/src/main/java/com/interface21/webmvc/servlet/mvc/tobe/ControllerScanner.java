package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instanceControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instanceControllers(Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllerMap = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            try {
                Object controller = controllerClass.newInstance();
                controllerMap.put(controllerClass, controller);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return controllerMap;
    };
}
