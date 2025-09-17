package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
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
        log.info("Initialized AnnotationHandlerMapping!");

        for (Object pkg : basePackage) {
            Reflections reflections = new Reflections(pkg);
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
            try {
                for (Class<?> clazz : classes) {
                    Object controller = clazz.getDeclaredConstructor().newInstance();
                    for (Method method : clazz.getMethods()) {
                        registerHandler(method, controller);
                    }
                }
            } catch (Exception e) {
                log.error("Initialize AnnotationHandlerMapping failed", e);
            }

        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerHandler(final Method method, final Object controller) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        String path = mapping.value();
        RequestMethod[] requestMethods = mapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(method, controller));
        }
    }
}
