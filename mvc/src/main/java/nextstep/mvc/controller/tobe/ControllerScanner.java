package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.exception.InstanceInitializationException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(final Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedControllers = reflections.getTypesAnnotatedWith(Controller.class);
        this.controllers = instantiateControllers(annotatedControllers);
    }

    public Map<Class<?>, Object> getControllers() {
        return Collections.unmodifiableMap(this.controllers);

    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> annotatedControllers) {
        Map<Class<?>, Object> map = new HashMap<>();
        for (Class<?> controller : annotatedControllers) {
            map.put(controller, getInstance(controller));
        }
        return map;
    }

    private Object getInstance(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new InstanceInitializationException();
        }
    }
}
