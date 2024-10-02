package com.interface21.webmvc.servlet.handler;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private Reflections reflections;

    public ControllerScanner(final Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .collect(Collectors.toMap(clazz -> clazz, this::instantiateController));
    }

    private Object instantiateController(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("기본 생성자를 찾을 수 없습니다: " + controllerClass.getName(), e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("컨트롤러를 인스턴스화할 수 없습니다: " + controllerClass.getName(), e);
        }
    }
}
