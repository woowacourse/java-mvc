package nextstep.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import nextstep.web.annotation.Controller;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getAnnotatedControllers() {
        Set<Class<?>> annotatedControllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(annotatedControllers);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : classes) {
            controllers.put(clazz, createInstance(clazz));
        }
        return controllers;
    }

    private Object createInstance(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("컨트롤러 생성 과정에서 예외가 발생했습니다.");
        }
    }
}
