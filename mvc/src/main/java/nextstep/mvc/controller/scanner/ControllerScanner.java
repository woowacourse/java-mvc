package nextstep.mvc.controller.scanner;

import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private static ControllerScanner INSTANCE = new ControllerScanner();

    private ControllerScanner() {
    }

    public static ControllerScanner getInstance() {
        return INSTANCE;
    }

    public Set<Class<?>> getAllAnnotations(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
