package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Object[] basePackage;

    public ControllerScanner(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> scan() {
        // 지정한 패키지에서 @Controller 클래스들을 탐색한다.
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses =
                reflections.getTypesAnnotatedWith(Controller.class);

        // Controller 클래스 정보와 실제 인스턴스를 함께 보관한다.
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, createController(controllerClass));
        }

        return controllers;
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            // Reflection으로 기본 생성자를 호출해 Controller 객체를 생성한다.
            return controllerClass
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(),
                    e
            );
        }
    }
}