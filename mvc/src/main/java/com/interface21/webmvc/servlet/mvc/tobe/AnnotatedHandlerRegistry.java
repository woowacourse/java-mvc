package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class AnnotatedHandlerRegistry {

    private final Map<Method, Object> methodRegistry = new HashMap<>();
    private final Map<Class<?>, Object> instanceRegistry = new HashMap<>();
    private final Object basePackage;

    public AnnotatedHandlerRegistry(final Object basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize(final Class<? extends Annotation> typeAnnotation,
                           final Class<? extends Annotation> methodAnnotation) {
        initializeInstance(typeAnnotation);
        initializeMethods(methodAnnotation);
    }

    private void initializeMethods(final Class<? extends Annotation> methodAnnotation) {
        final Reflections methodReflections = new Reflections(basePackage, Scanners.MethodsAnnotated);
        methodReflections.getMethodsAnnotatedWith(methodAnnotation)
                .forEach(method -> methodRegistry.put(method, getObject(method)));
    }

    private Object getObject(final Method method) {
        final Constructor<?> constructor = getConstructor(method.getDeclaringClass());
        return getObject(constructor);
    }

    private void initializeInstance(final Class<? extends Annotation> typeAnnotation) {
        final Reflections typeReflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        final List<Object> instances = typeReflections.getTypesAnnotatedWith(typeAnnotation)
                .stream()
                .map(this::getConstructor)
                .map(this::getObject)
                .toList();
        instances.forEach(instance -> instanceRegistry.put(instance.getClass(), instance));
    }

    private Object getObject(final Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException("인스턴스화 과정 예외 발생");
        }
    }

    private Constructor<?> getConstructor(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("잘못된 생성자 호출");
        }
    }

    public Set<Method> getMethods() {
        return methodRegistry.keySet();
    }

    public Object getInstance(final Method method) {
        if (!methodRegistry.containsKey(method)) {
            throw new IllegalArgumentException("등록되지 않은 메서드입니다.");
        }
        return methodRegistry.get(method);
    }
}
