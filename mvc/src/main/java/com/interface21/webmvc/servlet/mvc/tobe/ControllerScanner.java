package com.interface21.webmvc.servlet.mvc.tobe;

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

    public ControllerScanner(final Object[] basePackages) {
        this.basePackages = basePackages;
    }

    public Map<Class<?>, Object> getControllers() {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> eachClass : classes) {
            Object instance = createInstance(eachClass);
            controllers.put(eachClass, instance);
        }

        return controllers;
    }

    // 컨트롤러 인스턴스 생성
    private Object createInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.info("인스턴스 생성에 실패했습니다. {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
