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

    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllerMap = new HashMap<>();
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> c : controllerClasses) {
            try {
                Object instance = c.getDeclaredConstructor().newInstance();
                controllerMap.put(c, instance);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to instantiate @Controller: " + c, e);
            }
        }

        return controllerMap;
    }
}
