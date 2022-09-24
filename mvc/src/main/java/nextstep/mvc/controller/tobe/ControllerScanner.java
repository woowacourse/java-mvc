package nextstep.mvc.controller.tobe;

import java.util.HashSet;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;
    private final Set<Class<?>> controllers;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
        this.controllers = reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Set<Class<?>> getControllers() {
        return new HashSet<>(controllers);
    }
}
