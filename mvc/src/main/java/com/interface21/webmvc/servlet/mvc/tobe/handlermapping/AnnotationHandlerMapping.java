package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;

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
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        for (Class<?> controller : controllerScanner.getClasses()) {
            Method[] handlers = controller.getMethods();
            for (Method handler : handlers) {
                try {
                    Object controllerInstance = controllerScanner.getInstance(controller);
                    registerHandlerExecution(controllerInstance, handler);
                } catch (Exception e) {
                    log.error("error occurred while register " + handler.getName(), e);
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerExecution(Object controllerInstance, Method handler) {
        RequestMapping mappingAnnotation = handler.getAnnotation(RequestMapping.class);
        if (mappingAnnotation == null) {
            return;
        }
        RequestMethod[] requestMethods = mappingAnnotation.method();
        if (requestMethods.length == 0) { // method 미설정 시 모든 HTTP method 지원
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(mappingAnnotation.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, handler));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        String requestURI = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
