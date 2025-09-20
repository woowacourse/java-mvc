package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.interface21.context.stereotype.Controller;

public class ControllerScanner {

    Object[] basePackages;
    Map<Class<?>, Object> classInstances = new HashMap<>();

    public ControllerScanner(Object[] basePackages) {
        this.basePackages = basePackages;
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            Object instance;
            try {
                instance = controller.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            classInstances.put(controller, instance);
        }
    }

    public Set<Class<?>> getClasses() {
        return classInstances.keySet();
    }

    public Object getInstance(Class<?> controller) {
        return classInstances.get(controller);
    }
}
