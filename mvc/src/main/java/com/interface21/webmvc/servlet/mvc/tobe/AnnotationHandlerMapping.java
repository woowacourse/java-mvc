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

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        final Map<Class<?>, Object> controllers = controllerScanner.scan();

        for (final Object controller : controllers.values()) {
            addHandlers(controller);
        }

        log.info("Initialized {} controllers", controllers.size());
    }

    private void addHandlers(final Object controller) {
        final Method[] methods = controller.getClass().getDeclaredMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                addHandler(requestMapping, controller, method);
            }
        }
    }

    private void addHandler(final RequestMapping requestMapping, final Object controller, final Method method) {
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        final RequestMethod[] httpMethods = requestMapping.method();

        if (httpMethods.length == 0) {
            for (final RequestMethod rm : RequestMethod.values()) {
                handlerExecutions.put(new HandlerKey(requestMapping.value(), rm), handlerExecution);
            }
        } else {
            for (final RequestMethod rm : httpMethods) {
                handlerExecutions.put(new HandlerKey(requestMapping.value(), rm), handlerExecution);
            }
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey key = new HandlerKey(requestUri, requestMethod);
        return handlerExecutions.get(key);
    }
}
