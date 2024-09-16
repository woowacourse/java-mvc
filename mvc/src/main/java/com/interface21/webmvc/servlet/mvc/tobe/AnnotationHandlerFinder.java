package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.reflections.Reflections;

public class AnnotationHandlerFinder {

    private static final Class<? extends Annotation> HANDLER_CLASS_ANNOTATION = Controller.class;

    private final Reflections reflections;

    public AnnotationHandlerFinder(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public List<Handler> findHandlers(Class<? extends Annotation> annotation) {
        List<Method> methods = findAnnotatedMethods(annotation);
        return createHandlers(methods);
    }

    private List<Method> findAnnotatedMethods(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(HANDLER_CLASS_ANNOTATION).stream()
            .flatMap(controller -> Arrays.stream(controller.getMethods()))
            .filter(method -> method.isAnnotationPresent(annotation))
            .toList();
    }

    private List<Handler> createHandlers(List<Method> methods) {
        return methods.stream().map(this::createHandler).toList();
    }

    private Handler createHandler(Method handler) {
        Object instance = createHandlerInstance(handler);
        return new Handler(handler, instance);
    }

    private Object createHandlerInstance(Method handler) {
        try {
            return handler.getDeclaringClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("인스턴스를 생성할 수 없습니다.");
        }
    }
}
