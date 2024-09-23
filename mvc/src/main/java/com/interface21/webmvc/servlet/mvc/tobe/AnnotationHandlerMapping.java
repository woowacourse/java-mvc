package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            final Method[] methods = controller.getMethods();
            registerHandlerByMethods(controller, methods);
        }
    }

    private void registerHandlerByMethods(final Class<?> controller, final Method[] methods)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Method method : methods) {
            registerIfRequestMappingPresent(controller, method);
        }
    }

    private void registerIfRequestMappingPresent(final Class<?> controller, final Method method)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            registerHandlerByMethod(controller, method);
        }
    }

    private void registerHandlerByMethod(final Class<?> controller, final Method method)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = resolveUri(requestMapping);
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        registerHandler(controller, method, requestMethods, uri);
    }

    private String resolveUri(final RequestMapping requestMapping) {
        String uri = requestMapping.value();
        if (uri.isEmpty()) {
            return "/";
        }
        return uri;
    }

    private void registerHandler(final Class<?> controller, final Method method, final RequestMethod[] requestMethods,
                                 final String uri)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (final RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            checkDuplicatedHandlerKey(handlerKey);
            final Object instance = controller.getDeclaredConstructor().newInstance();
            final HandlerExecution handlerExecution = new HandlerExecution(method, instance);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private void checkDuplicatedHandlerKey(final HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("중복된 handlerKey가 존재합니다");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
