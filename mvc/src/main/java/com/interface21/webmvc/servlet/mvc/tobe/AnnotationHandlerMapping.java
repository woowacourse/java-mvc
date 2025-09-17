package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        try {
            Map<Class<?>, Object> controllers = controllerScanner.instantiateControllers();

            controllers.forEach((clazz, instance) ->
                    ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class))
                            .forEach(method -> registerHandlerExecution(instance, method))
            );
            log.info("Handler mappings initialized: {} handlers registered", handlerExecutions.size());
        } catch (final Exception e) {
            log.error("Failed to initialize handler", e);
        }
    }

    private void registerHandlerExecution(Object controllerInstance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution execution = new HandlerExecution(controllerInstance, method);

        Arrays.stream(resolveHttpMethods(requestMapping))
                .map(httpMethod -> new HandlerKey(requestMapping.value(), httpMethod))
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, execution));
    }

    private RequestMethod[] resolveHttpMethods(RequestMapping mapping) {
        return mapping.method().length == 0 ? RequestMethod.values() : mapping.method();
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        log.debug("Looking for handler for: {} {}", request.getMethod(), request.getRequestURI());

        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }
}
