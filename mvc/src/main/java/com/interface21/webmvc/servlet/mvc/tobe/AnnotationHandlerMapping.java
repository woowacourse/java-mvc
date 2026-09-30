package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        for (final Class<?> controller : reflections.getTypesAnnotatedWith(Controller.class)) {
            registerController(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(final Class<?> controller) {
        final Object instance = getInstance(controller);
        for (final Method method : controller.getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandler(method, instance);
            }
        }
    }

    private Object getInstance(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controller.getName(), e);
        }
    }

    private void registerHandler(final Method method, final Object instance) {
        final var annotation = method.getAnnotation(RequestMapping.class);
        final var handlerExecution = new HandlerExecution(instance, method);
        for (final RequestMethod requestMethod : getRequestMethods(annotation)) {
            handlerExecutions.put(new HandlerKey(annotation.value(), requestMethod), handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping annotation) {
        if (annotation.method().length == 0) {
            return RequestMethod.values();
        }
        return annotation.method();
    }

    public Object getHandler(final HttpServletRequest request) {
        return RequestMethod.findByName(request.getMethod())
                .map(requestMethod ->
                        handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod)))
                .orElse(null);
    }
}
