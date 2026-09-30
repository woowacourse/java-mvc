package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws Exception {
        Set<Object> controllerInstances = controllerScanner.scan();

        for (Object controllerInstance : controllerInstances) {
            for (Method controllerMethod : controllerInstance.getClass().getDeclaredMethods()) {
                RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);
                if (requestMapping == null) {
                    continue;
                }

                registerHandler(
                        controllerInstance,
                        controllerMethod,
                        requestMapping.value(),
                        requestMapping.method()
                );
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(requestMethod));

        return handlerExecutions.get(handlerKey);
    }

    private void registerHandler(
            Object handlerInstance,
            Method handlerMethod,
            String requestPath,
            RequestMethod[] requestMethods
    ) {
        // method가 생략되었다면 모든 HTTP Method를 대상으로 등록한다
        if (requestMethods.length == 0) {
            registerHandler(
                    handlerInstance,
                    handlerMethod,
                    requestPath,
                    RequestMethod.values()
            );
            return;
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestPath, requestMethod);
            handlerExecutions.put(
                    handlerKey,
                    new HandlerExecution(handlerInstance, handlerMethod)
            );
        }
    }
}
