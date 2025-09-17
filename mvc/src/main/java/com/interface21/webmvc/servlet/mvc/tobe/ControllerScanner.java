package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                .collect(Collectors.toMap(
                        controllerClass -> controllerClass,
                        this::createControllerInstance
                ));
    }

    private Object createControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            handleReflectionException(ex, controllerClass);
            return null;
        }
    }

    private void handleReflectionException(ReflectiveOperationException ex, Class<?> controllerClass) {
        if (ex instanceof InstantiationException || ex instanceof InvocationTargetException) {
            log.error("Controller 인스턴스 생성에 실패했습니다: {}", controllerClass.getName());
            throw new IllegalStateException("Controller 인스턴스 생성에 실패했습니다: " + controllerClass.getName(), ex);
        }
        if (ex instanceof IllegalAccessException) {
            log.error("메서드에 대한 접근 권한이 없습니다.");
            throw new IllegalStateException("메서드에 대한 접근 권한이 없습니다.", ex);
        }
        if (ex instanceof NoSuchMethodException) {
            log.error("기본 생성자를 찾을 수 없습니다: {}", controllerClass.getName());
            throw new IllegalStateException("기본 생성자를 찾을 수 없습니다: " + controllerClass.getName(), ex);
        }
        throw new IllegalStateException("알 수 없는 Reflection 예외가 발생했습니다.", ex);
    }
}
