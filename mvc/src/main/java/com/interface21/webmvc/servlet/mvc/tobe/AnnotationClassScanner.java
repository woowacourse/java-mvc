package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.annotation.Annotation;
import java.util.Set;
import org.reflections.Reflections;

public class AnnotationClassScanner {

    private final Reflections reflections;

    public AnnotationClassScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> scan(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
