package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object controller : controllerScanner.getControllers().values()) {
            registerHandlers(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(final Object controller) {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            RequestMethod[] requestMethods = requestMapping.method();
            if (requestMethods.length == 0) {
                requestMethods = RequestMethod.values();
            }

            final var handlerExecution = new HandlerExecution(controller, method);
            for (RequestMethod requestMethod : requestMethods) {
                final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        final var handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
