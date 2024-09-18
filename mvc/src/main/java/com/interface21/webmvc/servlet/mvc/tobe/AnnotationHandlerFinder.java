package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.reflections.Reflections;

public class AnnotationHandlerFinder {

    private static final Class<? extends Annotation> HANDLER_CLASS_ANNOTATION = Controller.class;
    private static final Class<? extends Annotation> HANDLER_METHOD_ANNOTATION = RequestMapping.class;

    private final Reflections reflections;
    private final Map<Class<?>, Object> handlerInstanceContainer;

    public AnnotationHandlerFinder(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
        this.handlerInstanceContainer = new HashMap<>();
    }

    public List<Handler> findHandlers() {
        List<Method> methods = findAnnotatedMethods(HANDLER_METHOD_ANNOTATION);
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

    private Handler createHandler(Method method) {
        Object instance = createHandlerInstance(method);
        return new Handler(method, instance);
    }

    private Object createHandlerInstance(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return handlerInstanceContainer.computeIfAbsent(declaringClass,
            (c) -> {
                try {
                    return c.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("인스턴스를 생성할 수 없습니다.");
                }
            });
    }
}
