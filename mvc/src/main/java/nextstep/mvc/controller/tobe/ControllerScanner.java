package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    private ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public static ControllerScanner from(final Object[] basePackages) {
        final Reflections reflections = new Reflections(basePackages);
        return new ControllerScanner(reflections);
    }

    public Map<Class<?>, Object> findControllers() throws Exception {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, newInstance(controllerClass));
        }
        return controllers;
    }

    private Object newInstance(final Class<?> clazz) throws Exception {
        final Constructor<?> constructor = clazz.getDeclaredConstructor();
        return constructor.newInstance();
    }
}
