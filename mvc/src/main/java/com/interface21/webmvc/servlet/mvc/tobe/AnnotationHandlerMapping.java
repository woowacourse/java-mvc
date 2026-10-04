package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(this::registerController);
    }

    private void registerController(final Class<?> controllerClass) {
        final Object controller = createController(controllerClass);

        for (Method method : controllerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            registerHandlerMethod(controller, method);
        }
    }

    private void registerHandlerMethod(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        for (RequestMethod requestMethod : getRequestMethods(requestMapping)) {
            addHandlerExecution(handlerExecution, requestMethod, requestMapping.value());
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create controller: " + controllerClass.getName(), e);
        }
    }

    private static RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            methods = RequestMethod.values();
        }
        return methods;
    }

    private void addHandlerExecution(final HandlerExecution handlerExecution, final RequestMethod method, final String url) {
        final HandlerKey handlerKey = new HandlerKey(url, method);
        final HandlerExecution previousHandler = handlerExecutions.putIfAbsent(handlerKey, handlerExecution);

        if (previousHandler != null) {
            throw new IllegalStateException("Duplicate handler mapping: " + handlerKey);
        }
    }
}
