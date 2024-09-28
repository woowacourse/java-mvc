package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, Object> handlers;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlers = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);

        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(this::handlerMapping);

        handlers.forEach((key, value) -> log.info("HandlerKey : {}, handler : {}", key, value));
    }

    private void handlerMapping(final Class<?> controllerClass) {
        final Object instance = getControllerInstance(controllerClass);

        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> methodMapping(instance, method));
    }

    private Object getControllerInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("컨트롤러 인스턴스 생성에 실패했습니다.");
        }
    }

    private void methodMapping(final Object instance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        List<RequestMethod> requestMethods = List.of(requestMapping.method());
        if (requestMethods.isEmpty()) {
            requestMethods = List.of(RequestMethod.values());
        }

        requestMethods.stream()
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .forEach(handlerKey -> handlers.put(handlerKey, instance));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.of(request.getMethod()));
        log.debug("Request Mapping Uri : {}", request.getRequestURI());

        return handlers.get(handlerKey);
    }
}
