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
        for (var pack : basePackage) {
            registerControllersInPackage(pack);
        }
    }

    private void registerControllersInPackage(Object pack) {
        Reflections reflections = new Reflections(pack.toString());
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            Object controllerInstance = instantiateController(controller);
            Method[] methods = controller.getDeclaredMethods();
            registerHandlerMethod(methods, controllerInstance);
        }
    }

    private void registerHandlerMethod(Method[] methods, Object controllerInstance) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String url = requestMapping.value();
                RequestMethod[] requestMethods = requestMapping.method();

                for (var requestMethod : requestMethods) {
                    HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                    HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
    }

    private static Object instantiateController(Class<?> controller) {
        Object controllerInstance;
        try {
            controllerInstance = controller.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("excetion occured" + e.getMessage());
        }
        return controllerInstance;
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.getRequestMethod(method);
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);

        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new IllegalArgumentException("handler not found");
        }
        return handlerExecution;
    }
}
