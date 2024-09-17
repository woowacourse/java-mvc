package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

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
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        //TODO 원뎁스로 어케 줄이지
        for (final Class<?> aClass : classes) {
            final Method[] declaredMethods = aClass.getDeclaredMethods();
            for (final Method declaredMethod : declaredMethods) {
                if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                    addHandler(aClass, declaredMethod);
                }
            }
        }
        log.info("init handlerExecutions: {}", handlerExecutions);
    }

    private void addHandler(final Class<?> aClass, final Method method) {
        final RequestMapping request = method.getAnnotation(RequestMapping.class);
        final var requestMethods = request.method().length == 0 ? RequestMethod.values() : request.method();
        for (final var requestMethod : requestMethods) {
            final var key = new HandlerKey(request.value(), requestMethod);
            validateDuplicate(key);
            final var handler = ConstructorGenerator.generate(aClass);
            handlerExecutions.put(key, new HandlerExecution(handler, method));
        }
    }

    private void validateDuplicate(final HandlerKey key) {
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalStateException("중복된 매핑 요청입니다.");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.getByValue(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
