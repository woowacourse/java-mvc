package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Map.of();

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final Map<HandlerKey, HandlerExecution> temporaryHandlerExecutions = new HashMap<>();

        Reflections reflections = new Reflections(basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);
        Map<Class<?>, Object> controllerClasses = controllerScanner.getControllers();
        
        for (Map.Entry<Class<?>, Object> entry : controllerClasses.entrySet()) {
            Class<?> controllerClass = entry.getKey();
            Object handler = entry.getValue();

            for (Method method : ReflectionUtils.getAllMethods(controllerClass,
                    ReflectionUtils.withAnnotation(RequestMapping.class))) {
                registerHandler(temporaryHandlerExecutions, handler, method);
            }
        }
        this.handlerExecutions = Map.copyOf(temporaryHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private void registerHandler(Map<HandlerKey, HandlerExecution> handlerExecutions, Object handler, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] httpMethods = requestMapping.method();

        HandlerExecution handlerExecution = new HandlerExecution(handler, method);

        String path = determinePath(requestMapping);

        if (httpMethods.length == 0) {
            for (RequestMethod requestMethod : RequestMethod.values()) {
                HandlerKey handlerKey = new HandlerKey(path, requestMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
            return;
        }
        for (RequestMethod requestMethod : httpMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private String determinePath(RequestMapping requestMapping) {
        if (requestMapping.value().isEmpty()) {
            return "/";
        }
        return requestMapping.value();
    }
}
