package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<Controller> CONTROLLER_CLASS = Controller.class;
    private static final Class<RequestMapping> REQUEST_MAPPING_CLASS = RequestMapping.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(CONTROLLER_CLASS);
        controllerClasses.forEach(this::findMethodWithAnnotation);
    }

    private void findMethodWithAnnotation(Class<?> controllerClass) {
        Method[] declaredMethods = controllerClass.getDeclaredMethods();
        Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(REQUEST_MAPPING_CLASS))
                .forEach(this::registerHandlerExecution);
    }

    private void registerHandlerExecution(Method method) {
        RequestMapping requestMapping = method.getAnnotation(REQUEST_MAPPING_CLASS);
        String value = requestMapping.value();
        RequestMethod[] methods = findRequestMethods(requestMapping);
        Arrays.stream(methods).forEach(requestMethod ->
                handlerExecutions.put(new HandlerKey(value, requestMethod), new HandlerExecution(method)));
    }

    private RequestMethod[] findRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods == null || methods.length == 0) {
            return RequestMethod.values();
        }
        return methods;
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(requestMethod));
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("No handler found for requestURI: " + requestURI + ", method: " + requestMethod);
        }
        return handlerExecutions.get(handlerKey);
    }
}
