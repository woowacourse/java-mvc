package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

public class AnnotatedControllers {

    private static final Class<? extends Annotation> CONTROLLER_ANNOTATION = Controller.class;

    private final List<AnnotatedController> values;

    public AnnotatedControllers(List<AnnotatedController> controllers) {
        this.values = controllers;
    }

    public static AnnotatedControllers from(Object... basePackage) {
        AnnotationClassScanner classScanner = new AnnotationClassScanner(basePackage);
        List<AnnotatedController> controllers = classScanner.scan(CONTROLLER_ANNOTATION)
                .stream()
                .map(AnnotatedController::from)
                .toList();

        return new AnnotatedControllers(controllers);
    }

    public List<Handler> createHandlers() {
        return values.stream()
                .map(AnnotatedController::createHandlers)
                .flatMap(Collection::stream)
                .toList();
    }
}
