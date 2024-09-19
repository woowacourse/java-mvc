package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new ConcurrentHashMap<>();
    }

    public void initialize() {
        Stream.of(basePackage)
                .forEach(this::initializeByPackage);
    }

    private void initializeByPackage(final Object basePackage) {
        final AnnotatedHandlerRegistry registry = new AnnotatedHandlerRegistry(basePackage);
        registry.initialize(Controller.class, RequestMapping.class);
        registry.getMethods()
                .forEach(method -> makeHandleKey(registry.getInstance(method), method));
    }

    private void makeHandleKey(final Object instance, final Method method) {
        final HandlerKeyCreator handlerKeyCreator = new HandlerKeyCreator(method);
        handlerKeyCreator.create()
                .forEach(handlerKey -> appendHandlerMapping(instance, method, handlerKey));
    }


    private void appendHandlerMapping(final Object instance, final Method method, final HandlerKey key) {
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalArgumentException("이미 존재하는 메서드 와 URL");
        }
        handlerExecutions.put(key, new HandlerExecution(instance, method));
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod method = RequestMethod.from(request.getMethod());
        final String requestURI = request.getRequestURI();
        final HandlerKey handlerKey = new HandlerKey(requestURI, method);
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("존재하지 않는 API");
        }
        return handlerExecutions.get(handlerKey);
    }
}
