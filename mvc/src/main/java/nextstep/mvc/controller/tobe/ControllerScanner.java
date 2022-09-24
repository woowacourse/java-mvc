package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        Map<Class<?>, Object> classes = new HashMap<>();
        for (Class<?> clazz : controller) {
            instantiateControllers(classes, clazz);
        }
        return classes;
    }

    private void instantiateControllers(final Map<Class<?>, Object> classes, final Class<?> clazz) {
        classes.put(clazz, instantiate(clazz));
    }

    private static Object instantiate(final Class<?> clazz) {
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
