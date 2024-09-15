package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
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
        Set<Class<?>> controllerClasses = scanControllerClasses();
        registerRequestMappingMethods(controllerClasses);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> scanControllerClasses() {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);

        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerRequestMappingMethods(Set<Class<?>> controllerClasses) {
        for (Class<?> controllerClass : controllerClasses) {
            String controllerBasePath = getControllerBasePath(controllerClass);
            registerMethods(controllerClass, controllerBasePath);
        }
    }

    private String getControllerBasePath(Class<?> controllerClass) {
        Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);

        return controllerAnnotation.value();
    }

    private void registerMethods(Class<?> controllerClass, String controllerBasePath) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerRequestMapping(method, controllerBasePath);
            }
        }
    }

    private void registerRequestMapping(Method method, String controllerBasePath) {
        RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
        String methodPath = requestMappingAnnotation.value();
        String fullPath = Paths.get(controllerBasePath, methodPath).toString();
        HandlerExecution handlerExecution = new HandlerExecution(method);

        registerHandlerExecution(requestMappingAnnotation.method(), fullPath, handlerExecution);
    }

    private void registerHandlerExecution(RequestMethod[] methods, String fullPath, HandlerExecution execution) {
        for (RequestMethod method : methods) {
            HandlerKey handlerKey = new HandlerKey(fullPath, method);

            handlerExecutions.put(handlerKey, execution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
