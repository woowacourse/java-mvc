package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ConcurrentMap<HandlerKey, AnnotationHandler> handlers;
    private final Object[] basePackage;

    public AnnotationHandlerMapping(ConcurrentMap<HandlerKey, AnnotationHandler> handlers,
                                    final Object... basePackage) {
        this.handlers = handlers;
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : typesAnnotatedWith) {
            Object controller = createNewInstance(controllerClass);
            List<Method> methods = getMethodsWithAnnotation(controllerClass);
            registerHandlerExecutions(methods, controller);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public AnnotationHandler getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlers.get(handlerKey);
    }

    @Override
    public boolean canHandle(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlers.containsKey(handlerKey);
    }

    private List<Method> getMethodsWithAnnotation(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private Object createNewInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException |
                 InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerHandlerExecutions(List<Method> methods, Object controller) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            AnnotationHandler handler = new AnnotationHandler(handlerExecution);

            Arrays.stream(getRequestMethods(requestMapping))
                    .map(httpMethod -> new HandlerKey(requestMapping.value(), httpMethod))
                    .forEach(handlerKey -> registerHandler(handlerKey, handler));
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] httpMethods = requestMapping.method();
        if (ArrayUtils.isEmpty(httpMethods)) {
            return RequestMethod.getRequestMethods();
        }
        return httpMethods;
    }

    private void registerHandler(HandlerKey handlerKey, AnnotationHandler handler) {
        if (handlers.containsKey(handlerKey)) {
            throw new IllegalArgumentException("중복된 url과 http method 입니다.");
        }
        handlers.put(handlerKey, handler);
    }
}
