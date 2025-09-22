package com.interface21.webmvc.servlet.mvc;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    public Map<Class<?>, Object> scan(final Object... basePackage) {
        if (basePackage == null || basePackage.length == 0) {
            throw new IllegalArgumentException("basePackage는 null이거나 비어 있을 수 없습니다.");
        }
        log.info("ControllerScanner scan 시작 - basePackage: {}", (Object) basePackage);
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllerClasses) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> controllerClass : controllerClasses) {
            final Object controllerInstance = instantiateController(controllerClass);
            controllers.put(controllerClass, controllerInstance);
        }
        log.info("ControllerScanner scan 완료 - 총 {}개의 컨트롤러 인스턴스화", controllers.size());
        if (log.isDebugEnabled()) {
            controllers.forEach((key, value) ->
                    log.debug("인스턴스화된 컨트롤러 - Class: {}, Instance: {}", key.getName(), value.getClass().getName()));
        }
        return Map.copyOf(controllers);
    }

    private Object instantiateController(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (final NoSuchMethodException exception) {
            log.error("Controller '{}'에서 기본 생성자를 찾을 수 없습니다.", controllerClass.getName(), exception);
            throw new IllegalStateException("기본 생성자가 없는 컨트롤러는 인스턴스화할 수 없습니다.", exception);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            log.error("Controller '{}' 인스턴스 생성에 실패했습니다.", controllerClass.getName(), exception);
            throw new IllegalStateException("컨트롤러 인스턴스 생성에 실패했습니다. 클래스 설정을 확인해주세요.", exception);
        }
    }
}
