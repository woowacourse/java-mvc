package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerKeys;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final HandlerKeys handlerKeys;
    private final ControllerContainer controllerContainer;

    public AnnotationHandlerMapping(final HandlerKeys handlerKeys, final ControllerContainer controllerContainer) {
        this.handlerKeys = handlerKeys;
        this.controllerContainer = controllerContainer;
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final var controllers = controllerContainer.getControllers();
        controllers.forEach(this::init);
    }

    private void init(final Class<?> controllerClass, final Object controllerInstance) {
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(this::hasRequestMapping)
                .forEach(method -> initialize(method, controllerInstance));
    }

    private boolean hasRequestMapping(final Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    private void initialize(final Method method, final Object controllerInstance) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        final String path = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();

        final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
        if (requestMethods.length == 0) {
            putAnnotationHandler(path, RequestMethod.all(), handlerExecution);
            return;
        }
        putAnnotationHandler(path, requestMethods, handlerExecution);
    }

    private void putAnnotationHandler(final String path, final RequestMethod[] methods, final HandlerExecution execution) {
        Arrays.stream(methods)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .forEach(handlerKey -> handlerKeys.put(handlerKey, execution));
    }

    public HandlerExecution getHandler(final HandlerKey key) {
        return handlerKeys.get(key);
    }
}
