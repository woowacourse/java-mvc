package com.interface21.webmvc.servlet.mvc.tobe.handler.mapping;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        var controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> controllers) {
        return controllers.stream()
                .collect(
                        toMap(
                                identity(),
                                this::instantiateController
                        )
                );
    }

    private Object instantiateController(Class<?> controllerClass) {
        try {
            final Constructor<?> constructor = getDeclaredConstructor(controllerClass);
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (final NoSuchMethodException e) {
            log.error("Exception : 컨트롤러 기본 생성자가 필요합니다. | {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static Constructor<?> getDeclaredConstructor(final Class<?> controllerClass) throws NoSuchMethodException {
        return controllerClass.getDeclaredConstructor();
    }
}
