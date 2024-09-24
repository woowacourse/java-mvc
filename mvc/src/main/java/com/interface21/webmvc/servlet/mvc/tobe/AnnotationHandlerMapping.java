package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerKeys;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerKeys handlerKeys;
    private final ControllerContainer controllerContainer;

    public AnnotationHandlerMapping(final HandlerKeys handlerKeys, final ControllerContainer controllerContainer,final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerKeys = handlerKeys;
        this.controllerContainer = controllerContainer;
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        final var controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.forEach(this::init);
    }

    private void init(final Class<?> controller) {
        Arrays.stream(controller.getDeclaredMethods())
                .filter(this::hasRequestMapping)
                .forEach(this::initialize);
    }

    private boolean hasRequestMapping(final Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    private void initialize(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        final String path = requestMapping.value();
        final Object controller = controllerContainer.getController(method.getDeclaringClass());
        if (requestMapping.method().length == 0) {
            putAnnotationHandler(path, RequestMethod.all(), controller, method);
        }
        putAnnotationHandler(path, requestMapping.method(), controller, method);
    }

    private void putAnnotationHandler(final String path, final RequestMethod[] methods, final Object controller, final Method method) {
        Arrays.stream(methods)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .forEach(handlerKey -> put(handlerKey, controller, method));
    }

    private void put(final HandlerKey handlerKey, final Object controller, final Method method) {
        handlerKeys.put(handlerKey, new HandlerExecution(controller, method));
    }

    public HandlerExecution getHandler(final HandlerKey key) {
        return handlerKeys.get(key);
    }
}
