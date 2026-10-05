package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    public Map<Class<?>, Object> scan(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        return controllerTypes.stream()
                .collect(Collectors.toMap(
                        controllerType -> controllerType,
                        this::createController
                ));
    }

    private Object createController(Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + controllerType.getName(), e);
        }
    }
}
