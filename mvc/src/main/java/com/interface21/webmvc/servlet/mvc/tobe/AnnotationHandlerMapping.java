package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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

    public void initialize() { // TODO refactor
        for (Object object : basePackage) {
            Reflections reflections = new Reflections(object);
            reflections.getMethodsAnnotatedWith(RequestMapping.class);
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> aClass : typesAnnotatedWith) {
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    addHandlerExecution(aClass, method);
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecution(Class<?> aClass, Method method) { // TODO refactor
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        Object controller = null;
        if (annotation == null) {
            return;
        }
        try {
             controller = aClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            return;
        }
        RequestMethod[] requestMethods = annotation.method();
        if (requestMethods == null) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.findByName(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
