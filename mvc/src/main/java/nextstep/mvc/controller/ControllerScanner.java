package nextstep.mvc.controller;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private Reflections reflections;

    public ControllerScanner(String packageName) {
        this.reflections = new Reflections(packageName);
    }

    public Map<Class<?>, Object> getControllers() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<Class<?>, Object> results = new HashMap<>();
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            results.put(controller, controller.getConstructor().newInstance());
        }
        return results;
    }
}
