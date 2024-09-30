package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final String ROOT_PATH = "/";

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        HandlerScanner handlerScanner = new HandlerScanner(basePackage);
        handlerScanner.getHandlers()
                .forEach(this::detectHandlerMethods);
    }

    private void detectHandlerMethods(Class<?> handlerType, Object handler) {
        List<Method> handlerMethods = ReflectionUtils.getMethods(handlerType, RequestMapping.class);

        for (Method handlerMethod : handlerMethods) {
            Controller controllerAnnotation = handlerType.getAnnotation(Controller.class);
            RequestMapping requestMappingAnnotation = handlerMethod.getAnnotation(RequestMapping.class);

            List<HandlerKey> handlerKeys = getHandlerKeys(controllerAnnotation, requestMappingAnnotation);
            HandlerExecution handlerExecution = new HandlerExecution(handler, handlerMethod);
            handlerKeys.forEach(handlerKey -> register(handlerKey, handlerExecution));
        }
    }

    private List<HandlerKey> getHandlerKeys(Controller controllerAnnotation, RequestMapping requestMappingAnnotation) {
        String path = combinePath(controllerAnnotation.value(), requestMappingAnnotation.value());
        RequestMethod[] requestMethods = getRequestMethods(requestMappingAnnotation);
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .toList();
    }

    private String combinePath(String parentPath, String childPath) {
        if (!parentPath.isBlank()) {
            return parentPath + childPath;
        }
        if (!childPath.isBlank()) {
            return childPath;
        }
        return ROOT_PATH;
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    private void register(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("Duplicated handler key : %s".formatted(handlerKey));
        }
        log.trace("register handler key: {}", handlerKey);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
