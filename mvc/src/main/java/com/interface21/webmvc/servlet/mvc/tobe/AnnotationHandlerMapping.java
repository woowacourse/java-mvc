package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final String ROOT_PATH = "/";

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        for (Class<?> handlerType : getHandlerTypes()) {
            detectHandlerMethods(handlerType);
        }
    }

    private Set<Class<?>> getHandlerTypes() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void detectHandlerMethods(Class<?> handlerType) {
        List<Method> handlerMethods = Arrays.stream(handlerType.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

        for (Method handlerMethod : handlerMethods) {
            List<HandlerKey> handlerKeys = getHandlerKeys(handlerType, handlerMethod);
            handlerKeys.forEach(handlerKey -> register(handlerKey, handlerMethod));
        }
    }

    private List<HandlerKey> getHandlerKeys(Class<?> handlerType, Method method) {
        Controller controllerAnnotation = handlerType.getAnnotation(Controller.class);
        RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
        String path = combinePath(controllerAnnotation.value(), requestMappingAnnotation.value());
        RequestMethod[] requestMethods = getRequestMethods(requestMappingAnnotation);
        return createHandlerKeys(path, requestMethods);
    }

    private String combinePath(String parentPath, String childPath) {
        if (!parentPath.isBlank()) {
            return parentPath + childPath;
        }
        if (!childPath.isBlank()) {
            return childPath;
        }
        return ROOT_PATH;
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    private List<HandlerKey> createHandlerKeys(String path, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .toList();
    }

    private void register(HandlerKey handlerKey, Method method) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("Duplicated handler key : %s".formatted(handlerKey));
        }
        log.trace("register handler key: {}", handlerKey);
        handlerExecutions.put(handlerKey, new HandlerExecution(method));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
