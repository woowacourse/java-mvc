package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner();
    }

    public void initialize() {
        for (Object packageName : basePackage) {
            scanAndRegisterControllers(packageName);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void scanAndRegisterControllers(Object packageName) {
        Set<Class<?>> controllerClasses = controllerScanner.scanControllers(packageName);

        for (Class<?> controllerClass : controllerClasses) {
            registerController(controllerClass);
        }
    }

    private void registerController(Class<?> controllerClass) {
        Object controllerInstance = controllerScanner.getInstance(controllerClass);
        Set<Method> methods = ReflectionUtils.getAllMethods(
                controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
        for (Method method : methods) {
            registerHandlerMethod(controllerInstance, method);
        }
    }

    private void registerHandlerMethod(Object controllerInstance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
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
