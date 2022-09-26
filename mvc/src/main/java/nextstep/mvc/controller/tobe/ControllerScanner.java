package nextstep.mvc.controller.tobe;

import static java.util.stream.Collectors.toMap;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private static final ControllerScanner INSTANCE = new ControllerScanner();

    private ControllerScanner() {
    }

    public static ControllerScanner getInstance() {
        return INSTANCE;
    }

    public Map<Class<?>, Object> scan(final Object... basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiate(classes);
    }

    private Map<Class<?>, Object> instantiate(Set<Class<?>> classes) {
        return classes.stream()
                .collect(toMap(Function.identity(), this::instantiate));
    }

    private Object instantiate(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
