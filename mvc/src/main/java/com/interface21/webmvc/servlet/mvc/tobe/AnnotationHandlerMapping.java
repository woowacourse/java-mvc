package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    public AnnotationHandlerMapping() {
        this.basePackage = new Object[]{"com.techcourse.controller"};
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        controllerTypes.forEach(this::mapControllerHandlers);

        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet()
                .forEach(handlerKey -> log.info("Path : {} {}, Controller : {}", handlerKey.requestMethod,
                        handlerKey.url,
                        handlerExecutions.get(handlerKey)));
    }

    private void mapControllerHandlers(Class<?> controllerType) {
        try {
            Object controller = controllerType.getConstructor().newInstance();
            Method[] handlers = controllerType.getDeclaredMethods();
            mapHandlerToExecution(controller, handlers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mapHandlerToExecution(Object controller, Method[] handlers) {
        Arrays.stream(handlers)
                .filter(handler -> handler.isAnnotationPresent(RequestMapping.class))
                .forEach(handler -> addMapper(handler, new HandlerExecution(controller, handler)));
    }

    private void addMapper(Method handler, HandlerExecution handlerExecution) {
        RequestMapping requestMapping = handler.getAnnotation(RequestMapping.class);
        List<HandlerKey> handlerKeys = createHandlerKeys(requestMapping);
        handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    private List<HandlerKey> createHandlerKeys(RequestMapping requestMapping) {
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == EMPTY_REQUEST_METHODS) {
            requestMethods = RequestMethod.values();
        }
        return Arrays.stream(requestMethods)
                .map(requestMethod -> HandlerKey.from(uri, requestMethod))
                .toList();
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
