package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        initialize();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void initialize() {
        Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            final Method[] methods = controller.getDeclaredMethods();
            registerRequestMappings(controller, methods);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerRequestMappings(final Class<?> controller, final Method[] methods) {
        for (Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }
            final RequestMethod[] httpMethods = requestMapping.method();
            registerHandlerExecution(controller, method, httpMethods, requestMapping);
        }
    }

    private void registerHandlerExecution(
            final Class<?> controller,
            final Method method,
            final RequestMethod[] httpMethods,
            final RequestMapping requestMapping
    ) {
        for (RequestMethod httpMethod : httpMethods) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), httpMethod);
            final HandlerExecution handlerExecution = createHandlerExecution(
                    controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private HandlerExecution createHandlerExecution(final Class<?> controller, final Method method) {
        try {
            Object controllerInstance = controller.getDeclaredConstructor().newInstance();
            return new HandlerExecution(controllerInstance, method);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalStateException("리플렉션 중 예외 발생");
        }
    }
}
