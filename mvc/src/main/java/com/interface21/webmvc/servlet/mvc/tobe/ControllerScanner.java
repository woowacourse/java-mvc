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

    public Set<Class<?>> findControllerClasses() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> controllerMap = new HashMap<>();
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

    public Map<Class<?>, Object> buildControllerRegistry() {
        Set<Class<?>> classes = findControllerClasses();
        return instantiateControllers(classes);
    }
}
