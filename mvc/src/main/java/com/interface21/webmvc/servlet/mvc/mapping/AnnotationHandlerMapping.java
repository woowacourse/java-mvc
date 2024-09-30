package com.interface21.webmvc.servlet.mvc.mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.handler.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        List<Method> requestMappingMethods = controllerScanner.scanControllerMethods();
        requestMappingMethods.forEach(this::registerHandlerExecution);

        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet()
                .forEach(handlerKey -> log.info("HandlerKey : {}, HandlerExecution : {}", handlerKey, handlerExecutions.get(handlerKey)));
    }

    private void registerHandlerExecution(Method method) {
        RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);

        RequestMethod[] methods = getTargetHttpMethods(requestMappingAnnotation);
        String url = requestMappingAnnotation.value();
        HandlerExecution handlerExecution = new HandlerExecution(method);

        registerHandlerExecution(methods, url, handlerExecution);
    }

    private RequestMethod[] getTargetHttpMethods(RequestMapping requestMappingAnnotation) {
        if (requestMappingAnnotation.method().length == 0) {
            return RequestMethod.values();
        }

        return requestMappingAnnotation.method();
    }

    private void registerHandlerExecution(RequestMethod[] methods, String url, HandlerExecution execution) {
        for (RequestMethod method : methods) {
            HandlerKey handlerKey = new HandlerKey(url, method);

            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("Duplicated handler key: " + handlerKey);
            }
            handlerExecutions.put(handlerKey, execution);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
