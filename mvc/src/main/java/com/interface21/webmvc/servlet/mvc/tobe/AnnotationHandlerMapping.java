package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private static final int EMPTY_REQUEST_METHODS = 0;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.stream()
                .map(Class::getDeclaredMethods)
                .forEach(this::mapHandlerToExecution);
    }

    private void mapHandlerToExecution(Method[] handlers) {
        Arrays.stream(handlers)
                .filter(handler -> handler.isAnnotationPresent(RequestMapping.class))
                .forEach(handler -> {
                    HandlerExecution handlerExecution = new HandlerExecution(handler);
                    addMapper(handler.getAnnotation(RequestMapping.class), handlerExecution);
                });
    }

    private void addMapper(RequestMapping requestMapping, HandlerExecution handlerExecution) {
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == EMPTY_REQUEST_METHODS) {
            requestMethods = RequestMethod.values();
        }
        Arrays.stream(requestMethods)
                .forEach(requestMethod -> handlerExecutions.put(HandlerKey.from(uri, requestMethod), handlerExecution));
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKey(request);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new IllegalArgumentException(
                    String.format("해당 요청에 대응하는 핸들러가 없습니다: %s %s", request.getMethod(), request.getRequestURI()));
        }
        return handlerExecution;
    }

    private HandlerKey createHandlerKey(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.find(request.getMethod());
        return HandlerKey.from(requestURI, requestMethod);
    }
}
