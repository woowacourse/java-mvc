package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() throws Exception {
        final Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(types);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> types) throws Exception {
        final Map<Class<?>, Object> result = new HashMap<>();
        for (Class<?> type : types) {
            final Constructor<?> declaredConstructor = type.getDeclaredConstructor();
            final Object instance = declaredConstructor.newInstance();
            result.put(type, instance);
        }
        return result;
    }
}
