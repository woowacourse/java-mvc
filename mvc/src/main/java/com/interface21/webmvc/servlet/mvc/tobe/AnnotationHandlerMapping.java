package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
                String value = annotation.value();
                RequestMethod[] method = annotation.method();

                if (method.length == 0) {
                    RequestMethod[] values = RequestMethod.values();
                    for (RequestMethod requestMethod : values) {
                        HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                        handlerExecutions.put(handlerKey, new HandlerExecution());
                    }
                } else {
                    for (RequestMethod requestMethod : method) {
                        HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                        handlerExecutions.put(handlerKey, new HandlerExecution());
                    }
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
