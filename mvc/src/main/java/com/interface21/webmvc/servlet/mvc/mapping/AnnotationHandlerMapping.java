package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.handler.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new ConcurrentHashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            final Class<?> controllerClass = entry.getKey();
            final Object controllerInstance = entry.getValue();
            registerRequestMappings(controllerInstance, controllerClass);
        }
    }

    private void registerRequestMappings(Object controllerInstance, Class<?> controllerClass) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            registerMethod(controllerInstance, method);
        }
    }

    private void registerMethod(Object controllerInstance, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        for (RequestMethod httpMethod : mapping.method()) {
            addHandlerExecution(controllerInstance, method, mapping, httpMethod);
        }
    }

    private void addHandlerExecution(Object controllerInstance, Method method, RequestMapping mapping,
                                     RequestMethod httpMethod) {
        HandlerKey key = new HandlerKey(mapping.value(), httpMethod);
        HandlerExecution newHandler = new HandlerExecution(controllerInstance, method);
        HandlerExecution existingHandler = handlerExecutions.putIfAbsent(key, newHandler);
        if (existingHandler != null) {
            throw new IllegalStateException("중복된 핸들러 매핑이 존재합니다: " + key);
        }
        log.info("Mapped [{} {}] to {}", httpMethod, mapping.value(), method.getName());

    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getServletPath(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }
}
