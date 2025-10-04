package com.interface21.webmvc.handlermapping.annotation;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.handlermapping.ControllerScanner;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackages) {
        this.controllerScanner = new ControllerScanner(basePackages);
        this.handlerExecutions = initializeHandlers();
    }

    private Map<HandlerKey, HandlerExecution> initializeHandlers() {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        List<Class<?>> controllerTypes = controllerScanner.getAllControllerTypes();

        for (Class<?> controllerType : controllerTypes) {
            Object controllerInstance = createControllerInstance(controllerType);

            List<Method> methods = ReflectionUtils.getAllMethods(controllerType, RequestMapping.class);

            methods.forEach(
                    method -> {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

                            for (RequestMethod requestMethod : requestMapping.method()) {
                                handlerExecutions.put(
                                        new HandlerKey(requestMapping.value(), requestMethod),
                                        handlerExecution
                                );
                            }
                        }
                    }
            );
        }

        log.info("Initialized AnnotationHandlerMapping!");
        return handlerExecutions;
    }

    private Object createControllerInstance(final Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        log.debug("Request Mapping Uri : {}", requestURI);

        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, method);
        return handlerExecutions.get(handlerKey);
    }
}
