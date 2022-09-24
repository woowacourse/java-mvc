package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
       return instantiateControllers(reflections.getTypesAnnotatedWith(Controller.class));
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> clz) {
        return clz.stream()
                .collect(Collectors.toMap(clas -> clas, this::createNewInstance));
    }

    private Object createNewInstance(final Class<?> clas) {
        try {
            return clas.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
