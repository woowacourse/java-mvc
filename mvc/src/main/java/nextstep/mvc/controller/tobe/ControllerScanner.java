package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import nextstep.mvc.exception.ControllerCreateException;
import nextstep.web.annotation.Controller;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getController() {
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerTypes);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerTypes) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerType : controllerTypes) {
            controllers.put(controllerType, createInstance(controllerType));
        }
        return controllers;
    }

    private static Object createInstance(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new ControllerCreateException();
        }
    }
}
