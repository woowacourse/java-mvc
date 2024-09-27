package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());
        requestMappingMethods.forEach(method -> addHandlerExecutions(controllers, method, method.getAnnotation(RequestMapping.class)));
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> classes) {
        return classes.stream().flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class)))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
        String url = requestMapping.value();
        RequestMethod[] methods = requestMapping.method().length != 0 ? requestMapping.method() : RequestMethod.values();
        List<HandlerKey> handlerKeys = mapHandlerKeys(url, methods);
        handlerKeys.forEach(handlerKey -> handlerExecutions.put(
                handlerKey, new HandlerExecution(controllers.get(method.getDeclaringClass()), method)));
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] methods) {
        return Arrays.stream(methods).map(method -> new HandlerKey(url, method)).toList();
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(requestMethod));
        return handlerExecutions.get(handlerKey);
    }
}
