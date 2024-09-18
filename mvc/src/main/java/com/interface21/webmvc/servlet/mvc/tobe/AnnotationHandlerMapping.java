package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(this::registerController);
    }

    private void registerController(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Stream.of(methods)
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> registerHandlerMethod(method, instance));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }

    private void registerHandlerMethod(Method method, Object instance) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(method, instance);
        String path = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        if (method.isEmpty()) {
            return findHandlerWithoutMethod(requestURI);
        }
        return findHandlerWithMethod(requestURI, method);
    }

    private HandlerExecution findHandlerWithMethod(String requestURI, String method) {
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        
        if (handlerExecution == null) {
            throw new IllegalArgumentException("요청에 맞는 Handler를 찾을 수 없습니다");
        }
        return handlerExecution;
    }

    private Object findHandlerWithoutMethod(String requestURI) {
        List<HandlerExecution> list = handlerExecutions.entrySet()
                .stream()
                .filter(entry -> entry.getKey().containsUrl(requestURI))
                .map(Entry::getValue)
                .toList();

        if (list.isEmpty()) {
            throw new IllegalArgumentException("요청에 맞는 Handler를 찾을 수 없습니다");
        }
        if (list.size() > 1) {
            throw new IllegalArgumentException("요청에 맞는 Handler가 여러 개입니다");
        }
        return list.get(0);
    }
}
