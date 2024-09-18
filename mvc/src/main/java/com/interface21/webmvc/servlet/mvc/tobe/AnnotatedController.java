package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotatedController {

    private static final Logger log = LoggerFactory.getLogger(AnnotatedController.class);

    private final Object controller;

    public AnnotatedController(Object controller) {
        this.controller = controller;
    }

    public static AnnotatedController from(Class<?> controllerClass) {
        return new AnnotatedController(createInstance(controllerClass));
    }

    private static Object createInstance(Class<?> controllerClass) {
        Constructor<?> controllerConstructor = getNoArgsConstructor(controllerClass);
        return createInstance(controllerConstructor);
    }

    private static Constructor<?> getNoArgsConstructor(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            log.error("no default constructor found for controller: {}", controllerClass.getName(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Object createInstance(Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            log.error("Failed to instantiate controller using constructor: {}", constructor, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<Handler> createHandlers() {
        return createControllerMethods().stream()
                .map(controllerMethod -> controllerMethod.createHandlers(controller))
                .flatMap(Collection::stream)
                .toList();
    }

    private List<ControllerMethod> createControllerMethods() {
        return Arrays.stream(controller.getClass().getMethods())
                .filter(this::isRequestMappingMethod)
                .map(ControllerMethod::new)
                .toList();
    }

    private boolean isRequestMappingMethod(Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }
}
