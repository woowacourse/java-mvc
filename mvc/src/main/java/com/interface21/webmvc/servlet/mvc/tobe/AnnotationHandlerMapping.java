package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Stream.of(basePackage)
                .map(Reflections::new)
                .map(reflections -> reflections.getTypesAnnotatedWith(Controller.class))
                .flatMap(Collection::stream)
                .forEach(this::scan);
    }

    private void scan(final Class<?> classType) {
        final Constructor<?> constructor = getConstructor(classType);
        final Object instance = getInstance(constructor);
        final Reflections reflections = new Reflections(classType, Scanners.MethodsAnnotated);
        reflections.getMethodsAnnotatedWith(RequestMapping.class)
                .forEach(method -> makeHandleKey(instance, method));
    }

    private Constructor<?> getConstructor(final Class<?> classType) {
        try {
            return classType.getDeclaredConstructor();
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("생성자 생성 실패");
        }
    }

    private Object getInstance(final Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (final InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException("인스턴스화 실패");
        }
    }

    private void makeHandleKey(final Object instance, final Method method) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = annotation.method();
        final String url = annotation.value();
        final List<HandlerKey> keys = createRequestMethod(requestMethods, url);
        keys.forEach(key -> appendHandlerMapping(instance, method, key));
    }

    private List<HandlerKey> createRequestMethod(final RequestMethod[] requestMethods, final String url) {
        if (requestMethods.length == 0) {
            return Stream.of(RequestMethod.values())
                    .map(requestMethod -> new HandlerKey(url, requestMethod))
                    .toList();
        }
        return Stream.of(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }

    private void appendHandlerMapping(final Object instance, final Method method, final HandlerKey key) {
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalArgumentException("이미 존재하는 메서드 와 URL");
        }
        handlerExecutions.put(key, new HandlerExecution(instance, method));
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod method = RequestMethod.from(request.getMethod());
        final String requestURI = request.getRequestURI();
        final HandlerKey handlerKey = new HandlerKey(requestURI, method);
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("존재하지 않는 API");
        }
        return handlerExecutions.get(handlerKey);
    }
}
