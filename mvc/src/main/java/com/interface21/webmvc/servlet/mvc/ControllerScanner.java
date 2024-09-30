package com.interface21.webmvc.servlet.mvc;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {
    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Object[] basePackages;

    public ControllerScanner(Object[] basePackages) {
        this.basePackages = basePackages;
    }

    public Map<Class<?>, Object> scan() {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Object basePackage : basePackages) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

            initializeControllers(controllerClasses, controllers);
        }

        log.info("Initialized AnnotationHandlerMapping!");
        return controllers;
    }

    private void initializeControllers(Set<Class<?>> controllerClasses, Map<Class<?>, Object> controllers) {
        for (Class<?> controllerClass : controllerClasses) {
            try {
                Object instance = controllerClass.getDeclaredConstructor().newInstance();
                controllers.put(controllerClass, instance);
            } catch (Exception e) {
                log.error("Handler의 Method를 매핑하는 과정에서 오류가 발생했습니다.", e);
            }
        }
    }
}
