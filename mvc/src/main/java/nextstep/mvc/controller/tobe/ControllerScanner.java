package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        return instantiateControllers(reflections.getTypesAnnotatedWith(Controller.class));
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classesAnnotatedWith) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : classesAnnotatedWith) {
            controllers.put(clazz, createNewInstance(clazz));
        }
        return controllers;
    }

    private Object createNewInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("클래스를 생성할 수 없습니다. [%s]", clazz.getName()));
        }
    }
}
