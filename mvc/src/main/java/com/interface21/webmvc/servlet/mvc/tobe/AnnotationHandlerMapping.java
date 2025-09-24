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
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : classes) {
            registerController(aClass);
        }
    }

    private void registerController(Class<?> aClass) {
        try {
            Object controllerInstance = aClass.getDeclaredConstructor().newInstance();
            Method[] methods = aClass.getMethods();

            registerMethod(methods, controllerInstance);
        } catch (Exception e) {
            log.error("Failed to initialize handler for class {}", aClass, e);
        }
    }

    private void registerMethod(Method[] methods, Object controllerInstance) {
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String uri = requestMapping.value();
            RequestMethod[] httpMethods = requestMapping.method();

            registerHttpMethod(controllerInstance, method, httpMethods, uri);
        }
    }

    private void registerHttpMethod(Object controllerInstance, Method method, RequestMethod[] httpMethods, String uri) {
        for (RequestMethod httpMethod : httpMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, httpMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(
                        request.getRequestURI(),
                        RequestMethod.valueOf(request.getMethod())
                )
        );
    }
}
