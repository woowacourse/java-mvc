package webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping;

import context.org.springframework.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    public Set<Class<?>> scan(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return controllers;
    }

    public Map<Class<?>, Object> createControllerInstance(Set<Class<?>> controllers) {
        try {
            Map<Class<?>, Object> instances = new HashMap<>();
            for (Class<?> controller : controllers) {
                instances.put(controller, controller.newInstance());
            }
            return instances;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
