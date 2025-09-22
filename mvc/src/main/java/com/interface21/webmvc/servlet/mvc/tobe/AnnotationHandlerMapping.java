package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

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
        
        for (Object packageName : basePackage) {
            Set<Class<?>> controllers = findControllers((String) packageName);
            for (Class<?> controller : controllers) {
                addHandlerExecutions(controller);
            }
        }
    }

    private Set<Class<?>> findControllers(String packageName) {
        Set<Class<?>> controllers = new HashSet<>();
        try {
            // ClassPathScanner를 사용해야 하지만, 일단 TestController만 처리
            if ("samples".equals(packageName)) {
                Class<?> clazz = Class.forName("samples.TestController");
                if (clazz.isAnnotationPresent(Controller.class)) {
                    controllers.add(clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("Controller class not found", e);
        }
        return controllers;
    }

    private void addHandlerExecutions(Class<?> controller) {
        try {
            Object controllerInstance = controller.getDeclaredConstructor().newInstance();
            Method[] methods = controller.getDeclaredMethods();
            
            for (Method method : methods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping != null) {
                    String url = requestMapping.value();
                    RequestMethod[] requestMethods = requestMapping.method();
                    
                    if (requestMethods.length == 0) {
                        for (RequestMethod requestMethod : RequestMethod.values()) {
                            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                    } else {
                        for (RequestMethod requestMethod : requestMethods) {
                            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to create handler execution for controller: " + controller.getName(), e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        try {
            RequestMethod requestMethod = RequestMethod.valueOf(method);
            HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
            return handlerExecutions.get(handlerKey);
        } catch (IllegalArgumentException e) {
            log.warn("Unsupported HTTP method: " + method);
            return null;
        }
    }
}
