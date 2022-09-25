package nextstep.mvc.controller.tobe;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class ControllerScanner {

    private final Reflections reflections;

    private ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public static ControllerScanner from(final Object... basePackage) {
        return new ControllerScanner(new Reflections(basePackage));
    }

    public Set<Class<?>> getControllers() {
        return reflections.get(TypesAnnotated.with(Controller.class).asClass());
    }
}
