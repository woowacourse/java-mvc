package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Object[] basePackage) {
        try {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controllerClass : controllerClasses) {
                controllers.put(controllerClass, controllerClass.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            throw new IllegalStateException("컨트롤러 클래스 인스턴스 생성에 실패했습니다.");
        }
    }

    public Set<Method> getAnnotationMethods(Class<? extends Annotation> annotation) {
        return getControllerClasses().stream()
                .flatMap(clazz -> Arrays.stream(clazz.getMethods()))
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> getControllerClasses() {
        return controllers.keySet();
    }

    public Object getController(Class<?> controllerClass) {
        return controllers.get(controllerClass);
    }
}
