package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AnnotationHandlerMapping implements MappingHandler {

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

        for (Class<?> clazz : controllerClasses) {
            processRequestMappingMethod(clazz);
        }
    }

    private void processRequestMappingMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            if (annotation == null) {
                continue;
            }
            createHandlerExecutions(Objects.requireNonNull(annotation));
        }
    }

    private void createHandlerExecutions(RequestMapping annotation) {
        String requestUri = annotation.value();
        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution();
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        String requestURI = request.getRequestURI();

        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
