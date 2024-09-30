package com.interface21.webmvc.servlet.mvc.annotation;

import java.util.Set;

import org.reflections.Reflections;

import com.interface21.context.stereotype.Controller;

public class ControllerScanner {
    private ControllerScanner() {}

    public static Set<Class<?>> getControllerClass(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
