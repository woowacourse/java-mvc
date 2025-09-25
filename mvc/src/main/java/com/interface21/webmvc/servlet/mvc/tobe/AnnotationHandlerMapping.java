package com.interface21.webmvc.servlet.mvc.tobe;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

    public AnnotationHandlerMapping(final String... basePackages) {
        this.controllerScanner = new ControllerScanner(basePackages);
    }

    public void initialize() {
        handlerExecutions.clear();

        final Map<Class<?>, Object> controllers = controllerScanner.scan();
        controllers.forEach(this::registerHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping with {} handler(s)", handlerExecutions.size());
    }

    private void registerHandlerExecutions(final Class<?> controllerClass, final Object controllerInstance) {
        final Set<Method> methods = getAllMethods(controllerClass, withAnnotation(RequestMapping.class));

        for (final Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            registerHandler(controllerInstance, method, requestMapping);
        }
    }

    private void registerHandler(final Object controllerInstance,
        final Method method,
        final RequestMapping requestMapping) {
        final String url = requestMapping.value();
        final RequestMethod[] declaredRequestMethods = requestMapping.method();
        final RequestMethod[] requestMethods = declaredRequestMethods.length == 0 ?
            RequestMethod.values() : declaredRequestMethods;

        if (!method.canAccess(controllerInstance)) {
            method.setAccessible(true);
        }

        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            final HandlerExecution previous = handlerExecutions.put(handlerKey, handlerExecution);

            if (previous != null) {
                log.warn("Duplicate handler mapping detected for {}. Overriding with {}", handlerKey, method);
            }
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        try {
            final RequestMethod requestMethod = RequestMethod.valueOf(method);
            final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
            return handlerExecutions.get(handlerKey);
        } catch (IllegalArgumentException e) {
            log.warn("Unsupported HTTP method: {}", method);
            return null;
        }
    }
}
