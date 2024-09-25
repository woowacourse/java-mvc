package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;

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
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controller : controllers) {
            final Method[] methods = controller.getMethods();
            registerHandlerByMethods(methods);
        }
        handlerExecutions.keySet().forEach(System.out::println);
    }

    private void registerHandlerByMethods(final Method[] methods) {
        for (final Method method : methods) {
            registerIfRequestMappingPresent(method);
        }
    }

    private void registerIfRequestMappingPresent(final Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            registerHandlerByMethod(method);
        }
    }

    private void registerHandlerByMethod(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String uri = resolveUri(requestMapping);
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        registerHandler(method, requestMethods, uri);
    }

    private String resolveUri(final RequestMapping requestMapping) {
        final String uri = requestMapping.value();
        if (uri.isEmpty()) {
            return "/";
        }
        return uri;
    }

    private void registerHandler(final Method method, final RequestMethod[] requestMethods, final String uri) {
        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            checkDuplicatedHandlerKey(handlerKey);
            final HandlerExecution handlerExecution = HandlerExecution.from(method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private void checkDuplicatedHandlerKey(final HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("중복된 handlerKey가 존재합니다");
        }
    }

    @Override
    public boolean hasHandler(final HttpServletRequest request) {
        final HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.containsKey(key);
    }

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response) {
        final HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        try {
            final ModelAndView modelAndView = handlerExecutions.get(key)
                    .handle(request, response);
            modelAndView.render(request, response);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }
}
