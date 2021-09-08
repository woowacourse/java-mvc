package nextstep.mvc.controller.tobe.scanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        Set<Class<?>> annotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);

        for (Object controller : annotatedWithController.toArray()) {
            Class<?> controllerClass = (Class<?>) controller;
            controllers.put(controllerClass, createInstance(controllerClass));
        }

        return controllers;
    }

    private Object createInstance(Class<?> instanceClass) {
        try {
            Constructor<?> declaredConstructor = instanceClass.getConstructor();
            return declaredConstructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException();
        }
    }
}
