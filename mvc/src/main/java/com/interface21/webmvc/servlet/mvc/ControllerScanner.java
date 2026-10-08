package com.interface21.webmvc.servlet.mvc;

import com.interface21.context.stereotype.Controller;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        // @Controller가 직접 붙은 클래스만 스캔한다 (하위 클래스 제외)
        return instantiateControllers(reflections.getTypesAnnotatedWith(Controller.class, true));
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers) {
        return controllers.stream()
                .collect(Collectors.toMap(Function.identity(), this::getInstance));
    }

    private Object getInstance(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controller.getName(), e);
        }
    }
}
