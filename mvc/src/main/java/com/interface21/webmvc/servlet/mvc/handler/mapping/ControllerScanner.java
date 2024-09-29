package com.interface21.webmvc.servlet.mvc.handler.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import com.interface21.context.stereotype.Controller;

public class ControllerScanner {

    private static final int NO_BASE_PACKAGE = 0;
    private static final String ALL_CLASS_PATH = "";

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Object... basePackage) {
        this.reflections = getReflections(basePackage);
    }

    private Reflections getReflections(Object... basePackage) {
        if (basePackage.length == NO_BASE_PACKAGE) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.forPackages(ALL_CLASS_PATH)
                    .addScanners(Scanners.TypesAnnotated);

            return new Reflections(builder);
        }
        return new Reflections(basePackage, Scanners.TypesAnnotated);
    }

    public void initialize() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, getInstance(controllerClass));
        }
    }

    private Object getInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Class의 인스턴스를 생성하는데 실패했습니다. Class = " + clazz.getCanonicalName());
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
