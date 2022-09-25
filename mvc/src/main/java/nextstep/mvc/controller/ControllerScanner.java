package nextstep.mvc.controller;

import java.util.Set;

import org.reflections.Reflections;

import nextstep.web.annotation.Controller;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> getAnnotatedControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
