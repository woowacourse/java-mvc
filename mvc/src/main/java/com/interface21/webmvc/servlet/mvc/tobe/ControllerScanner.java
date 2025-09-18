package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    public Map<Class<?>, Object> getControllers(Object ... basePackages) {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        Map<Class<?>, Object> controllers = new HashMap<>();
        try {
            for (Class<?> controller : controllerClasses) {
                Object handler = controller.getConstructor().newInstance();
                controllers.put(controller, handler);
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("기본 생성자가 존재하지 않습니다.");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("인스턴스 생성 중 예외가 발생했습니다.");
        }

        return controllers;
    }
}
