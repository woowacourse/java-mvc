package com.interface21.webmvc.servlet.mvc.tobe;

import static java.util.stream.Collectors.toMap;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private static final Class<Controller> CONTROLLER_TYPE = Controller.class;

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(CONTROLLER_TYPE);
        return classes.stream()
                .collect(toMap(clazz -> clazz, this::instantiate));
    }

    private Object instantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("컨트롤러를 인스턴스화 할 수 없습니다.: " + clazz, e);
        }
    }
}
