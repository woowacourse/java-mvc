package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final ControllerScanner controllerScanner) {
        this.controllerScanner = controllerScanner;
        this.handlerExecutions = new HashMap<>();
    }


    public void initialize() {
        controllerScanner.scan()
                .forEach(this::registerController);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(final Object controller) {
        final var controllerClass = controller.getClass();

        for (Method method : controllerClass.getMethods()) {
            if (isHandlerMethod(method)) {
                registerHandler(controller, method);
            }
        }
    }

    private boolean isHandlerMethod(final Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    private void registerHandler(final Object controller, final Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);

        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final var handlerExecution = new HandlerExecution(controller, method);

            if (!registerIfAbsent(handlerKey, handlerExecution)) {
                throw new IllegalStateException("Duplicate handler mapping: " + handlerKey);
            }
        }
    }

    private boolean registerIfAbsent(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        return handlerExecutions.putIfAbsent(handlerKey, handlerExecution) == null;
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        final var requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
