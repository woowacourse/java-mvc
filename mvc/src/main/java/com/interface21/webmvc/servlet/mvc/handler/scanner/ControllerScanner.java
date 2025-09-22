package com.interface21.webmvc.servlet.mvc.handler.scanner;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        try {
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            HashMap<Class<?>, Object> controllers = new HashMap<>();
            for (Class<?> controllerClass : controllerClasses) {
                controllers.put(controllerClass, controllerClass.getConstructor().newInstance());
            }
            return controllers;
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("컨트롤러를 구성하는 과정에서 예상치 못한 오류가 발생했습니다.");
        }
    }
}
