package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.Map;
import java.util.stream.Collectors;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                .collect(Collectors.toMap(
                        controllerClass -> controllerClass,
                        this::instantiateController));
    }

    private Object instantiateController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(), e);
        }
    }
}
