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

    public void initialize() throws InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object base : basePackage) {
            Reflections reflections = new Reflections((String) base);
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
            addHandlerExecutions(typesAnnotatedWith);
        }
    }

    private void addHandlerExecutions(Set<Class<?>> typesAnnotatedWith)
            throws InstantiationException, IllegalAccessException {
        for (Class<?> aClass : typesAnnotatedWith) {
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String url = requestMapping.value();
                    RequestMethod[] httpMethod = requestMapping.method();
                    for (RequestMethod requestMethod : httpMethod) {
                        Object o = aClass.newInstance();
                        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                        HandlerExecution handlerExecution = new HandlerExecution(o, method);
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        HandlerExecution handlerExecution = handlerExecutions.getOrDefault(handlerKey, null);

        if (handlerExecution == null) {
            throw new IllegalArgumentException("");
        }

        return handlerExecution;
    }
}
