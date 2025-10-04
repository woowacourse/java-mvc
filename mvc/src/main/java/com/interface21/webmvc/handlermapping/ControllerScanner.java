package com.interface21.webmvc.handlermapping;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.*;

public class ControllerScanner {

    private final String[] basePackages;
    private final Map<String, Reflections> reflections;

    public ControllerScanner(final String... basePackage) {
        Map<String, Reflections> reflections = new HashMap<>();
        for (String bp : basePackage) {
            reflections.put(bp, new Reflections(bp, Scanners.TypesAnnotated));
        }

        this.reflections = reflections;
        this.basePackages = basePackage;
    }

    public List<Class<?>> getAllControllerTypes() {
        List<Class<?>> controllerTypes = new ArrayList<>();

        for (String bp : basePackages) {
            Set<Class<?>> controllerTypesByBasePackage = findControllerTypes(bp);
            controllerTypes.addAll(controllerTypesByBasePackage);
        }

        return controllerTypes;
    }

    private Set<Class<?>> findControllerTypes(final String basePackage) {
        Reflections reflections = this.reflections.get(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
