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
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        scanControllersInPackage(basePackage);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void scanControllersInPackage(Object[] packages) {
        Reflections reflections = new Reflections(packages);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            registerController(controllerClass);
        }
    }

    private void registerController(Class<?> controllerClass) {
        try {
            Object controllerInstance = createControllerInstance(controllerClass);
            registerHandlerMethods(controllerClass, controllerInstance);
        } catch (Exception e) {
            log.error("Failed to initialize controller: " + controllerClass.getName(), e);
        }
    }

    private Object createControllerInstance(Class<?> controllerClass) throws Exception {
        return controllerClass.getConstructor()
                .newInstance();
    }

    private void registerHandlerMethods(Class<?> controllerClass, Object controllerInstance) {
        Method[] methods = controllerClass.getDeclaredMethods();

        for (Method method : methods) {
            if (isHandlerMethod(method)) {
                registerHandlerMethod(controllerInstance, method);
            }
        }
    }

    private boolean isHandlerMethod(Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    private void registerHandlerMethod(Object controllerInstance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] httpMethods = requestMapping.method();

        if (isAllMethodsSupported(httpMethods)) {
            registerForAllHttpMethods(url, controllerInstance, method);
            return;
        }

        registerForSpecificHttpMethods(url, httpMethods, controllerInstance, method);
    }

    private boolean isAllMethodsSupported(RequestMethod[] httpMethods) {
        return httpMethods.length == 0;
    }

    private void registerForAllHttpMethods(String url, Object controllerInstance, Method method) {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            addHandlerMapping(url, requestMethod, controllerInstance, method);
        }
    }

    private void registerForSpecificHttpMethods(
            String url, RequestMethod[] httpMethods,
            Object controllerInstance, Method method
    ) {
        for (RequestMethod requestMethod : httpMethods) {
            addHandlerMapping(url, requestMethod, controllerInstance, method);
        }
    }

    private void addHandlerMapping(
            String url, RequestMethod requestMethod,
            Object controllerInstance, Method method
    ) {
        HandlerKey key = new HandlerKey(url, requestMethod);
        HandlerExecution execution = new HandlerExecution(controllerInstance, method);
        handlerExecutions.put(key, execution);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String methodName = request.getMethod();

        try {
            RequestMethod requestMethod = RequestMethod.valueOf(methodName.toUpperCase());
            HandlerKey key = new HandlerKey(requestURI, requestMethod);

            return handlerExecutions.get(key);
        } catch (IllegalArgumentException e) {
            log.error("405 Method Not Allowed - URI exists but method not supported: {}", requestURI);

            return null;
        }
    }
}
