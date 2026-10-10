package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        controllerScanner.scan().forEach(this::registerController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(Class<?> controllerClass, Object controller) {
        for (Method method : controllerClass.getMethods()) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            if (mapping != null) {
                registerHandler(controller, method, mapping);
            }
        }
    }

    private void registerHandler(Object controller, Method method, RequestMapping mapping) {
        RequestMethod[] requestMethods = mapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        HandlerExecution execution = new HandlerExecution(controller, method);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
            handlerExecutions.put(key, execution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }
}
