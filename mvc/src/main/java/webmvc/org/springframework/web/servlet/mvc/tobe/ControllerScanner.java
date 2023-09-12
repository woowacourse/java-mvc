package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
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

    public Map<Class<?>, Object> controllers() throws Exception {
        Map<Class<?>, Object> controllers = new HashMap<>();

        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : typesAnnotatedWithController) {
            Object controller = clazz.getDeclaredConstructor().newInstance();
            controllers.put(clazz, controller);
        }

        return controllers;
    }
}
