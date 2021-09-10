package nextstep.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers;

    public ControllerScanner(final Object... basePackage) {
        reflections = new Reflections(basePackage);
        controllers = new HashMap<>();
        init();
    }

    private void init() {
        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        try {
            for (Class<?> clazz : typesAnnotatedWithController) {
                controllers.put(clazz, clazz.getDeclaredConstructor().newInstance());
            }
        } catch (final NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            LOGGER.error("controllers 스캔에 실패했습니다. : {}", e.getMessage());
            throw new RuntimeException("controllers 스캔에 실패했습니다.");
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return new HashMap<>(controllers);
    }
}
