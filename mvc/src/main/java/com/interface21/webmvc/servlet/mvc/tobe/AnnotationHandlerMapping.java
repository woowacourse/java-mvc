package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            registerController(controller);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }

    private void registerController(Class<?> controller) {
        Object controllerInstance;
        try {
             controllerInstance = controller.getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            log.error("Failed to initialize controller: {}", controller.getName(), e);
            throw new RuntimeException(e);
        }
        Method[] methods = controller.getDeclaredMethods();
        for (Method method : methods) {
            registerMethod(method, controllerInstance);
        }
    }

    private void registerMethod(Method method, Object controllerInstance) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        if (annotation == null) {
            return;
        }

        String url = annotation.value();
        RequestMethod[] requestMethods = annotation.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(url, requestMethod);
            handlerExecutions.put(key, new HandlerExecution(controllerInstance, method));
        }
    }
}
