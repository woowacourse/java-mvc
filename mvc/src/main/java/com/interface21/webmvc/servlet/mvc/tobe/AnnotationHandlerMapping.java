package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for(Class<?> controller : controllerClasses) {
            searchHandlerExceptions(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void searchHandlerExceptions(Class<?> controller){
        List<Method> mappedMethods = Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
        handlerExecutions.putAll(createHandlerExecutions(controller, mappedMethods));
    }

    private Map<HandlerKey, HandlerExecution> createHandlerExecutions(Class<?> controller, List<Method> mappedMethods) {
        return mappedMethods.stream()
                .flatMap(method -> createHandlerKeys(method).stream()
                        .map(handlerKey -> Map.entry(handlerKey, method)))
                .collect(Collectors.toMap(
                        Entry::getKey,
                        entry -> createHandlerExecution(controller, entry.getValue())));
    }

    private List<HandlerKey> createHandlerKeys(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String uri = annotation.value();
        RequestMethod[] requestMethods = annotation.method();
        if(requestMethods.length == 0) {
            return generateFromAllRequestMethods(uri);
        }
        return generateFromRequestMethod(uri,requestMethods);
    }

    private HandlerExecution createHandlerExecution(Class<?> controller, Method targetMethod) {
        try {
            Constructor<?> firstConstructor = controller.getDeclaredConstructor();
            Object executionTarget = firstConstructor.newInstance();
            return new HandlerExecution(executionTarget,targetMethod);
        } catch (Exception e) {
            throw new IllegalArgumentException("메서드 타입이 다른 controller로 초기화 했습니다.");
        }
    }

    private List<HandlerKey> generateFromAllRequestMethods(String uri) {
        return Arrays.stream(RequestMethod.values())
                .map(requestMethod -> new HandlerKey(uri, requestMethod))
                .toList();
    }

    private List<HandlerKey> generateFromRequestMethod(String uri, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(uri,requestMethod))
                .toList();
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI().toString(),
                RequestMethod.findMethod(request.getMethod()));
        return handlerExecutions.get(key);
    }
}
