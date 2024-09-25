package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Stream.of(basePackage)
                .forEach(this::initializeByPackage);
    }

    private void initializeByPackage(final String basePackage) {
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

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final RequestMethod method = RequestMethod.from(request.getMethod());
        final String requestURI = request.getRequestURI();
        final HandlerKey handlerKey = new HandlerKey(requestURI, method);
        return handlerExecutions.get(handlerKey);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        final AnnotationHandlerMapping that = (AnnotationHandlerMapping) object;
        return Arrays.equals(basePackage, that.basePackage) && Objects.equals(handlerExecutions,
                that.handlerExecutions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(handlerExecutions);
        result = 31 * result + Arrays.hashCode(basePackage);
        return result;
    }
}
