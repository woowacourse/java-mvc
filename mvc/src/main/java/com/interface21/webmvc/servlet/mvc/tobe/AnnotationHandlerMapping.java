package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        scanRequestMappings();
    }

    private void scanRequestMappings() {
        Arrays.stream(basePackage)
                .forEach(obj -> {
                    Reflections reflections = new Reflections(obj);
                    Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
                    for (Class<?> clazz : classes) {
                        Arrays.stream(clazz.getMethods())
                                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                                .forEach(this::addHandlerExecution);
                    }
                });
    }

    private void addHandlerExecution(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = annotation.method();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
