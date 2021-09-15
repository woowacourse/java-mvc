package nextstep.mvc.controller;

import com.google.common.collect.Maps;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> preInitiatedControllers = reflections.getTypesAnnotatedWith(Controller.class);
        return scanController(preInitiatedControllers);
    }

    private Map<Class<?>, Object> scanController(Set<Class<?>> controllers) {
        Map<Class<?>, Object> newControllers = Maps.newHashMap();
        try {
            for (Class<?> controller : controllers) {
                newControllers.put(controller, controller.getConstructor().newInstance());
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error(e.getMessage());
        }
        return newControllers;
    }
}
