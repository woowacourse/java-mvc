package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
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
        for (final Object packageName : basePackage) {
            final Reflections reflections = new Reflections(packageName.toString());

            final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

            for (final Class<?> clazz : controllerClasses) {
                registerHandlerMethods(clazz);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerMethods(final Class<?> controllerClass) {
        try {
            final Object controller = controllerClass.getDeclaredConstructor().newInstance();

            for (final Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    registerHandlerMethod(method, controller);
                }
            }
        } catch (Exception e) {
            log.error("Error registering handler methods for class: {}", controllerClass.getName(), e);
        }
    }

    private void registerHandlerMethod(final Method method, final Object controller) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        registerMapping(controller, method, url, requestMethods);
    }

    private void registerMapping(
            final Object controller,
            final Method method,
            final String url,
            final RequestMethod[] requestMethods
    ) {
        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String httpMethod = request.getMethod();

        final RequestMethod requestMethod = RequestMethod.valueOf(httpMethod);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
