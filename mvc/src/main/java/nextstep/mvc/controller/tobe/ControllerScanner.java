package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
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

    public List<Method> findMethodsWithRequestMapping(Class<?> controller) {
        return filterRequestMapping(List.of(controller.getMethods()));
    }

    private List<Method> filterRequestMapping(List<Method> methods) {
        return methods.stream()
            .filter(it -> it.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }
}
