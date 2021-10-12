package nextstep.mvc.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Reflections reflections;

    public ControllerScanner(Object object) {
        this.reflections = new Reflections((String) object);
    }

    public Map<Class<?>, Object> getController() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        final Set<Class<?>> annotatedControllers = reflections
                .getTypesAnnotatedWith(Controller.class);
        for (Class<?> annotatedController : annotatedControllers) {
            try {
                controllers
                        .put(annotatedController,
                                annotatedController.getConstructor().newInstance());
            } catch (Exception e) {
                log.debug("Error: {}", e.getMessage(), e);
            }
        }
        return controllers;
    }

    public Set<Method> getMethods(Set<Class<?>> controller) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> clazz : controller) {
            methods.addAll(
                    ReflectionUtils.getAllMethods(
                            clazz,
                            ReflectionUtils.withAnnotation(RequestMapping.class)
                    )
            );
        }
        return methods;
    }
}
