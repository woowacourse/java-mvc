package nextstep.mvc.controller.tobe;

import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Set<Class<?>> getAnnotationController() {
        final Reflections reflections = new Reflections(this.basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
