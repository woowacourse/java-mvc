package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = initialize(controllerScanner.scan());
    }

    public Map<HandlerKey, HandlerExecution> initialize(Map<Class<?>, Object> controllerByClasses) {
        log.info("Initialized AnnotationHandlerMapping!");

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Class<?> controllerClass : controllerByClasses.keySet()) {
            Object controller = controllerByClasses.get(controllerClass);

            Set<Method> allMethods = ReflectionUtils.getAllMethods(controllerClass,
                    ReflectionUtils.withAnnotation(RequestMapping.class));
            for (Method method : allMethods) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping == null) {
                    continue;
                }

                RequestMethod[] requestMethods = getRequestMethods(requestMapping);
                for (RequestMethod requestMethod : requestMethods) {
                    HandlerExecution handlerExecution = handlerExecutions.putIfAbsent(
                            new HandlerKey(requestMapping.value(), requestMethod),
                            new HandlerExecution(controller, method));
                    if (handlerExecution != null) {
                        throw new IllegalStateException("Duplicate handler mapping detected");
                    }
                }
            }
        }
        return Collections.unmodifiableMap(handlerExecutions);
    }

    private RequestMethod[] getRequestMethods(RequestMapping methodMapping) {
        RequestMethod[] requestMethods = methodMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.findByName(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
