package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> classes = new HashSet<Class<?>>(reflections.getTypesAnnotatedWith(Controller.class));

        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {

        return classes.stream()
                .collect(Collectors.toMap(clazz -> clazz, this::createInstance));
    }

    private Object createInstance(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
