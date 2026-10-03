package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> classObjectMap = controllerScanner.getControllers();

        for (Entry<Class<?>, Object> entry : classObjectMap.entrySet()) {
            Method[] methods = entry.getKey().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    RequestMethod[] requestMethods = requestMapping.method();
                    if (requestMethods.length == 0) {
                        requestMethods = RequestMethod.values();
                    }

                    Object controller = entry.getValue();
                    HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                        if (handlerExecutions.containsKey(handlerKey)) {
                            throw new IllegalArgumentException("Duplicate handler mapping: " + handlerKey);
                        }
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(url, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
