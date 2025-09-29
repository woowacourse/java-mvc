package com.interface21.webmvc.servlet.mvc.handlermapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controller : controllerClasses) {
            initializeController(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeController(final Class<?> controller) {
        try {
            final Object controllerInstance = controller.getDeclaredConstructor().newInstance();
            final Method[] methods = controller.getDeclaredMethods();
            for (final Method method : methods) {
                addHandlerExecutions(controllerInstance, method);
            }
        } catch (Exception e) {
            log.error("Error initializing controller", e);
        }
    }

    private void addHandlerExecutions(final Object controller, final Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String path = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping.method());

        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Mapped {} {} to {}", requestMethod, path, method);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMethod[] requestMethods) {
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))
        );
    }
}
