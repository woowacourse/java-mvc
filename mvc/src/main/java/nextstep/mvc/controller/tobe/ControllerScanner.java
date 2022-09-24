package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
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
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(types);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> types) {
        return types.stream()
                .collect(Collectors.toMap(it -> it, it -> {
                    try {
                        return it.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException();
                    }
                }));
    }
}
