package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final var clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
            controllers.put(clazz, instantiateController(clazz));
        }
        return controllers;
    }

    private Object instantiateController(Class<?> clazz) {
        try {
            final var constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
            throw new IllegalArgumentException("해당 Controller의 인스턴스화에 실패하였습니다.");
        }
    }
}
