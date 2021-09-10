package nextstep.mvc.controller.scanner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private ControllerScanner() {}

    public static Set<Class<?>> scanController(String... basePackages) {
        return scanController(Arrays.asList(basePackages));
    }

    public static Set<Class<?>> scanController(List<String> basePackages) {
        return basePackages.stream()
            .map(basePackage -> new Reflections(basePackage))
            .map(reflections -> reflections.getTypesAnnotatedWith(Controller.class))
            .flatMap(controller -> controller.stream())
            .collect(Collectors.toSet());
    }
}
