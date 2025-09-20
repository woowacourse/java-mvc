package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    // ControllerScanner: 컨트롤러를 찾아서 인스턴스를 생성하는 역할
    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers;

    private ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
        this.controllers = Collections.unmodifiableMap(scanAndInstantiate());
    }

    public static ControllerScanner from(final Object[] basePackages) {
        return new ControllerScanner(new Reflections(basePackages));
    }

    /**
     * 애플리케이션 시작 시 1회만 호출되어 컨트롤러들을 스캔하고 인스턴스화한다.
     */
    private Map<Class<?>, Object> scanAndInstantiate() {
        final Set<Class<?>> controllerClasses =
                reflections.getTypesAnnotatedWith(Controller.class); // @Controller 애노테이션이 붙은 클래스들 검색

        // 리플렉션 테스트에서 Class로 어떻게 객체를 생성하는지 참고하자.
        final Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, newInstance(controllerClass));
        }
        return controllers;
    }

    private Object newInstance(final Class<?> clazz) {
        try {
            // 컨트롤러 인스턴스를 생성(newInstance)
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "[ControllerScanner] 컨트롤러 인스턴스 생성 실패: " + clazz.getName(), e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
