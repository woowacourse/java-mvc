package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Map<String, Reflections> reflections;

    public ControllerScanner(final String... basePackage) {
        Map<String, Reflections> reflections = new HashMap<>();
        for (String bp : basePackage) {
            reflections.put(bp, new Reflections(bp, Scanners.TypesAnnotated));
        }

        this.reflections = reflections;
    }

    public Set<Class<?>> getControllerTypes(String basePackage) {
        Reflections reflections = this.reflections.get(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Object getControllerInstance(Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
