package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> aClass : classes) {
            final Method[] declaredMethods = aClass.getDeclaredMethods();
            for (final Method declaredMethod : declaredMethods) {
                final RequestMapping request = declaredMethod.getAnnotation(RequestMapping.class);
                final RequestMethod[] requestMethods = request.method();
                for (final RequestMethod requestMethod : requestMethods) {
                    //TODO 생성자 찾는거 다른 객체에게 책임 넘기기
                    final Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
                    final Object handler = declaredConstructor.newInstance();
                    //TODO 중복 검사
                    handlerExecutions.put(new HandlerKey(request.value(), requestMethod),
                            new HandlerExecution(handler, declaredMethod));
                }
            }
        }
        log.info("init handlerExecutions: {}", handlerExecutions);
    }

    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.getByValue(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
