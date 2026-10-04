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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllers) {
            Method[] methods = clazz.getDeclaredMethods();
            try {
                Object controller = clazz.getConstructor().newInstance();
                for (Method method : methods) {
                    HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String url = annotation.value();
                        RequestMethod[] requestMethods = annotation.method();
                        if (requestMethods.length == 0) {
                            requestMethods = RequestMethod.values();
                        }

                        for (RequestMethod requestMethod : requestMethods) {
                            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                    }
                }

            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, method);
        return handlerExecutions.get(handlerKey);
    }
}
