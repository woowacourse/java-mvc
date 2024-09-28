package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.webmvc.servlet.exception.ControllerScanException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Object[] basePackage;
    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Object... basePackage) {
        this.basePackage = basePackage;
        scanControllers();
    }

    private void scanControllers() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedControllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        annotatedControllerTypes.forEach(this::addController);
    }

    private void addController(Class<?> annotatedControllerType) {
        try {
            Object controllerInstance = annotatedControllerType.getConstructor().newInstance();
            controllers.put(annotatedControllerType, controllerInstance);
        } catch (Exception e) {
            throw new ControllerScanException("핸들러 초기화에서 예외가 발생했습니다.", e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return Collections.unmodifiableMap(controllers);
    }
}
