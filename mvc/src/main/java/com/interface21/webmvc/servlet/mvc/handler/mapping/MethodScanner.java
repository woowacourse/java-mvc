package com.interface21.webmvc.servlet.mvc.handler.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodScanner {

    private final Class<?> targetClass;

    public MethodScanner(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Set<Method> findAllByAnnotation(Class<? extends Annotation> annotation) {
        return Arrays.stream(targetClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }
}
