package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackage) {
        this(new Reflections(basePackage));
    }

    private ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        return instantiateControllers(reflections.getTypesAnnotatedWith(Controller.class));
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : classes) {
            controllers.put(clazz, createNewController(clazz));
        }
        return controllers;
    }

    private static Object createNewController(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalArgumentException("컨트롤러를 생성할 수 없스비다.");
        }
    }
}
