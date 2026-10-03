package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import java.util.Map;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .collect(Collectors.toMap(aClass -> aClass, this::createInstance));
    }

    private Object createInstance(Class<?> aClass) {
        try {
            return ReflectionUtils.accessibleConstructor(aClass).newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + aClass.getName(), e);
        }
    }
}
