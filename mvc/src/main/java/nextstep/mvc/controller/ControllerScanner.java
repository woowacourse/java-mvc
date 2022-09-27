package nextstep.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    private ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public static ControllerScanner of(final Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return new ControllerScanner(reflections);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        return classes.stream()
                .collect(Collectors.toMap(clazz -> clazz, this::instantiate));
    }

    private Object instantiate(final Class<?> clazz) {
        try {
            return clazz.getConstructor()
                    .newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Controller instantiation failed.", e);
        }
    }
}
