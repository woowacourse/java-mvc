package com.interface21.webmvc.servlet.mvc.handler;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.get(handlerKey);
    }

    @Override
    public void initialize() {
        final Set<Class<?>> controllers = scanController();
        final Set<Method> requestMappingMethods = scanRequestMappingAnnotatedMethod(controllers);
        registerRequestMapping(requestMappingMethods);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> scanController() {
        return Arrays.stream(basePackage)
                .flatMap(targetPackage -> {
                    final Reflections reflections = new Reflections(targetPackage);
                    return reflections.getTypesAnnotatedWith(Controller.class).stream();
                }).collect(Collectors.toSet());
    }

    private Set<Method> scanRequestMappingAnnotatedMethod(Set<Class<?>> controllers) {
        return controllers.stream()
                .map(this::scanRequestMappingAnnotatedMethod)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private Set<Method> scanRequestMappingAnnotatedMethod(Class<?> controller) {
        final Method[] methods = controller.getMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void registerRequestMapping(Set<Method> methods) {
        methods.forEach(this::registerRequestMapping);
    }

    private void registerRequestMapping(Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        Arrays.stream(requestMapping.method()).forEach(requestMethod -> {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(generateInstance(method), method);
            handlerExecutions.put(handlerKey, handlerExecution);
        });
    }

    private Object generateInstance(Method method) {
        try {
            return method.getDeclaringClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
