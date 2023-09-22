package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import static java.util.stream.Collectors.toList;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerMappingException;

public class AnnotationHandlerMapping implements HandlerMapping<HandlerExecution> {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        for (Object packagePath : basePackage) {
            initHandlerExecutions(packagePath);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandlerExecutions(Object packagePath) {
        Set<Class<?>> clazz = getClassByAnnotation(packagePath, Controller.class);
        for (Class<?> aClass : clazz) {
            List<Method> methods = getMethodsByAnnotation(aClass, RequestMapping.class);
            Object instance = getInstance(aClass);
            for (Method method : methods) {
                HandlerExecution handlerExecution = new HandlerExecution(instance, method);
                putHandlerExecution(handlerExecution, method);
            }
        }
    }

    private <T extends Annotation> Set<Class<?>> getClassByAnnotation(Object packagePath, Class<T> annotationClass) {
        Reflections reflections = new Reflections(packagePath);
        return reflections.getTypesAnnotatedWith(annotationClass);
    }

    private <T extends Annotation> List<Method> getMethodsByAnnotation(Class<?> aClass, Class<T> annotationClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
            .filter(it -> it.isAnnotationPresent(annotationClass))
            .collect(toList());
    }

    private Object getInstance(Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e
        ) {
            throw new HandlerMappingException();
        }
    }

    private void putHandlerExecution(HandlerExecution handlerExecution, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
