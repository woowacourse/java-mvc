package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers;


    public ControllerScanner(Object... objects) {
        this.reflections = new Reflections(objects);
        this.controllers = mapControllers();
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }

    private Map<Class<?>, Object> mapControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .collect(Collectors.toMap(clazz -> clazz, this::instantiate));
    }

    private Object instantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("클래스의 인스턴스를 생성하는 데 실패했습니다.");
        }
    }
}
