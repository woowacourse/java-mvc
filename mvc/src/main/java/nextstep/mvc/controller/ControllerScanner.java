package nextstep.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private static final Class<Controller> CONTROLLER_CLASS = Controller.class;

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        return instantiateControllers(reflections.getTypesAnnotatedWith(CONTROLLER_CLASS));
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controllerClass : classes) {
            controllers.put(controllerClass, getInstance(controllerClass));
        }
        return controllers;
    }

    private Object getInstance(Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("인스턴스를 찾을 수 없습니다.");
        }
    }
}
