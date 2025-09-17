package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private Map<Class<?>, Object> controllerInstances;

    public Map<Class<?>, Object> getControllerInstances() {
        return controllerInstances;
    }

    public Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers) {
        controllerInstances = new HashMap<>();

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
