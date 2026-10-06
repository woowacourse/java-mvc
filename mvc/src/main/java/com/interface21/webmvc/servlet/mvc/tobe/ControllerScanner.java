package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

//TODO 컴포넌트 스캐너로 변경
public class ControllerScanner {

    private final Object[] basePackages;

    public ControllerScanner(Object... basePackages) {
        this.basePackages = basePackages;
    }

    public List<Object> scan() {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        return controllerTypes.stream()
                .map(this::createController)
                .toList();
    }

    private Object createController(Class<?> controllerType) {
        try {
            return ReflectionUtils.accessibleConstructor(controllerType).newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(
                    "컨트롤러 인스턴스를 생성할 수 없습니다: " + controllerType.getName(),
                    exception
            );
        }
    }
}
