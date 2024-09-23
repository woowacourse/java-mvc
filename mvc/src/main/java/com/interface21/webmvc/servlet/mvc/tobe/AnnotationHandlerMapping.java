package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
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

        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(controller -> {
                    try {
                        registerControllerHandlers(controller);
                    } catch (Exception e) {
                        log.error("Failed to register controller handlers", e);
                    }
                });
    }

    private void registerControllerHandlers(Class<?> controllerClass) {
        Method[] methods = controllerClass.getMethods();

        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerHandlerExecutions(
                        createControllerInstance(controllerClass), method
                ));
    }

    private Object createControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create controller instance", e);
        }
    }

    private void registerHandlerExecutions(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();

        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMapping.method().length == 0) {
            requestMethods = RequestMethod.values();
        }

        Arrays.stream(requestMethods)
                .forEach(requestMethod -> registerHandlerExecution(url, requestMethod, controller, method));
    }

    private void registerHandlerExecution(String url, RequestMethod requestMethod, Object controller, Method method) {
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
