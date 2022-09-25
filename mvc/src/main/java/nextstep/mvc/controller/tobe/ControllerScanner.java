package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import nextstep.web.annotation.Controller;

class ControllerScanner {

    private final Reflections reflections;

    ControllerScanner(final Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> classesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(classesAnnotatedWith);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classesAnnotatedWith) {
        return classesAnnotatedWith.stream()
            .collect(Collectors.toMap(clazz -> clazz, this::instantiateController, (a, b) -> b));
    }

    private Object instantiateController(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
            InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Controller initialize failed: " + clazz.getName());
        }
    }
}
