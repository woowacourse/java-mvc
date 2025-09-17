package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : controllerClasses) {
            try {
                final Object controllerInstance = clazz.getConstructor().newInstance();
                final Method[] methods = clazz.getMethods();

                for (Method method : methods) {
                    addMethodToHandlerExecutions(method, controllerInstance);
                }
                log.info("Initialized AnnotationHandlerMapping");
            } catch (Exception e) {
                log.error("Failed to initialize handler: " + clazz.getName(), e);
            }
        }
    }

    private void addMethodToHandlerExecutions(final Method method, final Object controllerInstance) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String uri = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();

        final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

        Arrays.stream(requestMethods.length == 0 ? RequestMethod.values() : requestMethods)
                .forEach(requestMethod -> handlerExecutions.put(
                        new HandlerKey(uri, requestMethod), handlerExecution));
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        log.debug("Looking for handler for: {} {}", request.getMethod(), request.getRequestURI());

        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }
}
