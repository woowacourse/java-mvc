package nextstep.mvc.controller.scanner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private static ControllerScanner INSTANCE = new ControllerScanner();

    private ControllerScanner() {
    }

    public static ControllerScanner getInstance() {
        return INSTANCE;
    }

    public Set<Class<?>> getAllAnnotations(String... basePackage) {
        return Arrays.stream(basePackage)
            .map(Reflections::new)
            .map(reflections -> reflections.getTypesAnnotatedWith(Controller.class))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }
}
