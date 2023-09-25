package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    private ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public static ControllerScanner from(Object... basePackages) {
        return new ControllerScanner(new Reflections(basePackages));
    }

    public Map<Class<?>, Object> controllers() {
        Map<Class<?>, Object> controllers = new HashMap<>();

        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : typesAnnotatedWithController) {
            Object controller = instantiate(clazz);
            controllers.put(clazz, controller);
        }

        return controllers;
    }

    private Object instantiate(Class<?> clazz) {
        try {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalArgumentException("Controller 초기화에 실패했습니다.", e);
        }
    }
}
