package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        Map<Class<?>, Object> classByInstance = new HashMap<>();
        for (Class<?> clazz : classes) {
            try {
                Object instance = clazz.getConstructor().newInstance();
                classByInstance.put(clazz, instance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return classByInstance;
    }
}
