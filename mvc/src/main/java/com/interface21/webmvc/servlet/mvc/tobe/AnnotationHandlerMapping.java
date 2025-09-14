package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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
        for (Object base : basePackage) {
            Reflections reflections = new Reflections((String) base);
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
            addHandlerExecutions(typesAnnotatedWith);
        }
    }

    private void addHandlerExecutions(Set<Class<?>> typesAnnotatedWith) {
        try {
            for (Class<?> controllerClass : typesAnnotatedWith) {
                processController(controllerClass);
            }
        } catch (Exception e) {
            log.error("Failed to initialize handler mappings", e);
            throw new RuntimeException("Failed to initialize handler mappings.", e);
        }
    }

    private void processController(Class<?> controllerClass) throws Exception {
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                addMappingsForMethod(controllerInstance, method);
            }
        }
    }

    private void addMappingsForMethod(Object controllerInstance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] httpMethods = requestMapping.method();

        for (RequestMethod requestMethod : httpMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);

            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("Duplicate mapping found: " + handlerKey);
            }

            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Mapped [{}] -> {}", handlerKey, method.getName());
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        HandlerExecution handlerExecution = handlerExecutions.getOrDefault(handlerKey, null);

        if (handlerExecution == null) {
            throw new IllegalArgumentException(
                    "No handler found for " + request.getMethod() + " " + request.getRequestURI()
            );
        }

        return handlerExecution;
    }
}
