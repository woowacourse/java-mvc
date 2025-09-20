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
    private final ComponentScanner componentScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final ComponentScanner componentScanner, final Object... basePackage) {
        this.basePackage = basePackage;
        this.componentScanner = componentScanner;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Map<Class<?>, Object> controllers = componentScanner.scan(Controller.class, basePackage);
        for (Class<?> clazz : controllers.keySet()) {
            for (Method controllerMethod : clazz.getDeclaredMethods()) {
                registerMethod(controllerMethod, clazz);
            }
        }
    }

    private void registerMethod(final Method controllerMethod, final Object controller) {
        RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        String path = requestMapping.value();
        RequestMethod[] requestMappingMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMappingMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            HandlerExecution existing = handlerExecutions.putIfAbsent(handlerKey,
                    new HandlerExecution(controller, controllerMethod));
            if (existing != null) {
                throw new IllegalStateException("Duplicate mapping");
            }
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
