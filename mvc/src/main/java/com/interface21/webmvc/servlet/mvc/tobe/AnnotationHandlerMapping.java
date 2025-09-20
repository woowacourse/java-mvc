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
import java.util.List;
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
        setHandlerExecutions();
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void setHandlerExecutions() {
        handlerExecutions.clear();

        final Set<Class<?>> controllers = findControllerTypes();
        for (Class<?> controller : controllers) {
            registerControllerMappings(controller);
        }
    }

    private Set<Class<?>> findControllerTypes() {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerControllerMappings(Class<?> controller) {
        try {
            final List<Method> methods = findRequestMappingMethods(controller);
            final Object executor = controller.getDeclaredConstructor().newInstance();
            for (Method method : methods) {
                registerMethodMapping(executor, method);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private List<Method> findRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void registerMethodMapping(Object controller, Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String path = requestMapping.value();
        final RequestMethod[] requestMethods = resolveRequestMethods(requestMapping);

        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String requestMethod = request.getMethod();
        final HandlerKey key = new HandlerKey(requestURI, RequestMethod.valueOf(requestMethod.toUpperCase()));
        return handlerExecutions.get(key);
    }
}
