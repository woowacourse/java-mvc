package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> handlers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> handler : handlers) {
            Object handlerInstance = extractHandlerInstance(handler);
            for (Method handlerMethod : handler.getDeclaredMethods()) {
                registerHandlerExecution(handlerInstance, handlerMethod);
            }
        }
    }

    private void registerHandlerExecution(final Object handlerInstance, final Method handlerMethod) {
        List<HandlerKey> handlerKeys = extractHandlerKeys(handlerMethod);
        HandlerExecution handlerExecution = new HandlerExecution(handlerInstance, handlerMethod);

        handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    private List<HandlerKey> extractHandlerKeys(final Method handlerMethod) {
        if (!handlerMethod.isAnnotationPresent(RequestMapping.class)) {
            return Collections.emptyList();
        }
        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        RequestMethod[] supportedRequestMethods = extractSupportedRequestMethod(requestMapping.method());
        return Arrays.stream(supportedRequestMethods)
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .toList();
    }

    private RequestMethod[] extractSupportedRequestMethod(final RequestMethod[] method) {
        if (method.length == 0) {
            return RequestMethod.values();
        }
        return method;
    }

    private static Object extractHandlerInstance(final Class<?> handler)  {
        try {
            return handler.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.info("클래스의 인스턴스를 찾는데 실패하였습니다.", e);
            return null;
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey requestHandlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(requestHandlerKey);
    }
}
