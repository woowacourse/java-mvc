package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
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
        try {
            log.info("Initialized AnnotationHandlerMapping!");

            final Reflections reflections = new Reflections(basePackage);
            ControllerScanner controllerScanner = new ControllerScanner(reflections);

            Map<Class<?>, Object> scannedControllers = controllerScanner.getClasses();
            scannedControllers.keySet()
                    .forEach(type -> registerHandlerExecutionByType(type, scannedControllers.get(type)));

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }

    private void registerHandlerExecutionByType(Class<?> type, Object instance) {
        Set<Method> methods = ReflectionUtils.getAllMethods(type,
                ReflectionUtils.withAnnotation(RequestMapping.class));
        methods.forEach(method -> registerHandlerExecutionForMethod(type, method, instance));
    }

    private void registerHandlerExecutionForMethod(Class<?> type, Method method, Object instance) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        for (RequestMethod httpMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), httpMethod);
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
