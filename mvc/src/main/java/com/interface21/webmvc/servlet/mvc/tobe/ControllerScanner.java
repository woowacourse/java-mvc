package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import com.interface21.context.stereotype.Controller;

public class ControllerScanner {

    private static final Class<Controller> CONTROLLER = Controller.class;

    private final Reflections typesAnnotatedReflections;

    public ControllerScanner(final Object... basePackage) {
        this.typesAnnotatedReflections = new Reflections(basePackage, Scanners.TypesAnnotated);
    }

    public Set<Class<?>> getControllers() {
        return typesAnnotatedReflections.getTypesAnnotatedWith(CONTROLLER);
    }
}
