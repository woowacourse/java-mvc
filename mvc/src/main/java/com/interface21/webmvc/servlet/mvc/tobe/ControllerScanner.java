package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() throws Exception {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerClasses) throws Exception {
        final Map<Class<?>, Object> controllerInstances = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            final Object controllerInstance = controllerClass.getConstructor().newInstance();
            controllerInstances.put(controllerClass, controllerInstance);
        }
        return controllerInstances;
    }
}
