package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    public static final int EMPTY = 0;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .flatMap(clazz -> Arrays.stream(clazz.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::mappingHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void mappingHandlerExecutions(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping.method().length == EMPTY) {
            HandlerKey.listOf(requestMapping, RequestMethod.values())
                    .forEach(handlerKey -> addHandlerExecution(method, handlerKey));
            return;
        }
        final HandlerKey handlerKey = HandlerKey.from(requestMapping);
        addHandlerExecution(method, handlerKey);
    }

    private void addHandlerExecution(final Method method, final HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("이미 존재하는 요청 매핑입니다.");
        }
        final HandlerExecution handlerExecution = HandlerExecution.from(method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = getHandlerKey(request);
        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey getHandlerKey(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return new HandlerKey(requestUri, requestMethod);
    }
}
