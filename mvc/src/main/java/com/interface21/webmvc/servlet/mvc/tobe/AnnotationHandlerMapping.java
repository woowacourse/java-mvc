package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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

        for (Object pkg : basePackage) {
            Reflections reflections = new Reflections(pkg);

            for (Class<?> clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
                Object controller = getController(clazz);
                for (Method controllerMethod : clazz.getDeclaredMethods()) {
                    RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);
                    String path = requestMapping.value();
                    RequestMethod[] requestMappingMethods = requestMapping.method();
                    for (RequestMethod requestMethod : requestMappingMethods) {
                        HandlerKey handlerKey = new HandlerKey(path, requestMethod);
                        handlerExecutions.put(handlerKey, new HandlerExecution(controller, controllerMethod));
                    }
                }
            }
        }
    }

    private Object getController(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        throw new UnsupportedOperationException();
    }
}
