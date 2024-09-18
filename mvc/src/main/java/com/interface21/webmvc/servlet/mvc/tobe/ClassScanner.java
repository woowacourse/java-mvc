package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.reflections.Reflections;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;

public class ClassScanner {
    private final Reflections reflections;

    public ClassScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public List<Method> findHandlingMethods() {
        return  reflections.getTypesAnnotatedWith(Controller.class).stream()
                .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }
}
