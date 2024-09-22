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

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object pack : basePackage) {
            Reflections reflections = new Reflections(pack);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controller : controllers) {
                initializeHandlerExecutions(controller);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeHandlerExecutions(Class<?> controller) {
        List<Method> requestMappingMethods = getRequestMappingMethods(controller);
        for (Method requestMappingMethod : requestMappingMethods) {
            RequestMapping annotation = requestMappingMethod.getAnnotation(RequestMapping.class);
            setHandlerExecutions(controller, requestMappingMethod, annotation);
        }
    }

    public List<Method> getRequestMappingMethods(Class<?> controller) {
        Method[] methods = controller.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void setHandlerExecutions(Class<?> controller, Method method, RequestMapping annotation) {
        String url = annotation.value();
        RequestMethod[] requestMethods = annotation.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
