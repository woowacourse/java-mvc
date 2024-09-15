package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.exception.HandlerMappingException;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllerTypeClass = reflections.getTypesAnnotatedWith(Controller.class);
        log.info("Controller type class count : {}", controllerTypeClass.size());

        try {
            mapControllerToHandler(controllerTypeClass);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new HandlerMappingException(e.getMessage());
        }
    }

    private void mapControllerToHandler(Set<Class<?>> controllerTypeClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Class<?> controllerClass : controllerTypeClass) {
            Object controller = controllerClass.getDeclaredConstructor().newInstance();
            mapControllerExecutionMethod(controller);
        }
    }

    private void mapControllerExecutionMethod(Object controller) {
        for (Method method : controller.getClass().getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandlerExecution(method, controller);
            }
        }
    }

    private void registerHandlerExecution(Method method, Object controller) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
