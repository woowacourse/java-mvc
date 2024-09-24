package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;


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
        controllerClasses.forEach(this::register);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(requestMethod));
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (Objects.isNull(handlerExecution)) {
            log.warn("No handler found for request URI: {} and method: {}", requestURI, requestMethod);
            return Optional.empty();
        }
        return Optional.of(handlerExecution);
    }

    private void register(final Class<?> controllerClass) {
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> assignHandlerExecution(controllerClass, method));
    }

    private void assignHandlerExecution(Class<?> controllerClass, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerClass, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }
}
