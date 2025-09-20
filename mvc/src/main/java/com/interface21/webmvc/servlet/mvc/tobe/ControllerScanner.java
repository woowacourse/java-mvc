package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Object[] basePackages;

    public ControllerScanner(final Object... basePackages) {
        this.basePackages = basePackages;
    }

    public Map<Class<?>, Object> getControllers() {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> clazz : classes) {
            Object instance = toInstance(clazz);
            if (controllers.containsKey(clazz)) {
                throw new IllegalArgumentException("이미 존재하는 컨트롤러 인스턴스 입니다.");
            }
            controllers.put(clazz, instance);
        }
        return controllers;
    }

    private Object toInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor()
                .newInstance();
        } catch (Exception e) {
            log.info("인스턴스 {} 생성 실패: {}", clazz.getName(), e.getMessage());
            throw new RuntimeException("인스턴스 생성에 실패했습니다: " + clazz, e);
        }
    }
}
