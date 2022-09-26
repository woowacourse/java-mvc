package nextstep.mvc.controller.tobe;

import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    private ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public static ControllerScanner from(Object... basePackage) {
        return new ControllerScanner(new Reflections(basePackage));
    }

    public Set<Class<?>> findAllControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
