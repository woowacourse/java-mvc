package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return getControllers(controllers);
    }

    private Map<Class<?>, Object> getControllers(final Set<Class<?>> controller) {
        return controller.stream()
                .collect(Collectors.toMap(Function.identity(), this::instantiate));
    }

    private Object instantiate(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException
                 | InstantiationException
                 | InvocationTargetException
                 | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
