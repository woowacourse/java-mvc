package com.interface21.webmvc.servlet.mvc.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ControllerScanner {

    private static final Class<? extends Annotation> CONTROLLER_ANNOTATION = Controller.class;

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
    }

    public List<Method> scanControllerMethods() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(CONTROLLER_ANNOTATION);
        return scanMethods(controllerClasses);
    }

    private List<Method> scanMethods(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Stream::of)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }
}
