package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(Object[] basePackage) {
        this.basePackage = basePackage;
    }

    public Set<Object> scan() {
        try {
            Reflections reflections = new Reflections(basePackage);
            Set<Object> controllers = new HashSet<>();

            for (Class<?> type : reflections.getTypesAnnotatedWith(Controller.class)) {
                controllers.add(type.getDeclaredConstructor().newInstance());
            }

            return controllers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
