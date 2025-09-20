package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        try {
            return instantiateControllers(controllerClasses);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            log.info("모든 컨트롤러에 대한 인스턴스화에 실패했습니다 : {}", e.getMessage());
        }
        return null;
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            Object instance = controllerClass.getConstructor().newInstance();
            controllers.put(controllerClass, instance);
        }

        return controllers;
    }
}
