package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Set;

public class ControllerScanner {

    public static Set<Class<?>> getControllerTypes(String basePackage) {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public static Object getControllerInstance(Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
