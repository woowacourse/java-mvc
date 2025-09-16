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
        Set<Class<?>> controllers = findControllers();
        registerHandlerExecutions(controllers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> findControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerHandlerExecutions(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            try {
                Object controllerInstance = controller.getDeclaredConstructor().newInstance();
                addHandlers(controllerInstance);
            } catch (Exception e) {
                log.error("Failed to instantiate controller: {}", controller, e);
            }
        }
    }

    private void addHandlers(Object controllerInstance) {
        Method[] methods = controllerInstance.getClass().getDeclaredMethods();
        for (Method method : methods) {
            addHandler(controllerInstance, method);
        }
    }

    private void addHandler(Object controllerInstance, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping == null) {
            return;
        }

        String uri = mapping.value();
        RequestMethod[] requestMethods = getRequestMethods(mapping);
        for (RequestMethod requestMethod : requestMethods) {
            if (requestMethod == null) {
                continue;
            }
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.debug("Mapped [{}] {} -> {}", requestMethod, uri, method);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping mapping) {
        RequestMethod[] requestMethods = mapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        String uri = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            log.debug("No handler found for {}", handlerKey);
            return null;
        }
        return handlerExecution;
    }
}
