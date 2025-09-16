package com.interface21.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

public class AnnotationScanner {

    // TODO : 이겨 남겨놔야 할까?
    public static Set<Class<?>> scanClassesOfBasePackage(final Class<? extends Annotation> annotation,
                                                         final Object... basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    public static List<Method> scanMethods(final Class<?> clazz,
                                           final Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .toList();
    }
}
