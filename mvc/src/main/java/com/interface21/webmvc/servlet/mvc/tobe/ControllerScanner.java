package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        System.out.println("size: "+controllerClasses.size());
        return instantiateController(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateController(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .collect(Collectors.toMap(
                        clazz -> clazz,
                        clazz -> {
                            try {
                                return clazz.getConstructor().newInstance();
                            } catch (Exception e) {
                                throw new RuntimeException("컨트롤러를 생성할 수 없습니다.", e);
                            }
                        }
                ));
    }
}
