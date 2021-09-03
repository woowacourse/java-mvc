package nextstep.mvc.controller.scanner;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {
    public static Set<Class<?>> getAllControllers(String... basePackage) {
        return Arrays.stream(basePackage)
            .map(Reflections::new)
            .map(reflections -> reflections.getTypesAnnotatedWith(Controller.class))
            .flatMap(Collection::stream)
            .collect(toSet());
    }
}
