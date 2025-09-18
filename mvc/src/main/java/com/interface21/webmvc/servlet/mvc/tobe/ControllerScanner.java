package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllerInstances() {
        Map<Class<?>, Object> controllerInstances = instantiateControllers(
                reflections.getTypesAnnotatedWith(Controller.class));
        return controllerInstances;
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers) {
        Map<Class<?>, Object> controllerInstances = new HashMap<>();

        for (Class<?> controller : controllers) {
            Object controllerInstance;
            try {
                controllerInstance = controller.getConstructor().newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to instantiate controller: " + controller.getName(), e);
            }
            controllerInstances.put(controller, controllerInstance);
        }
        return controllerInstances;
    }
}
