package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);


    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final HashMap<Method, Object> cache = new HashMap<>();


    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        List<Method> declaredMethods = reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods()))
                .toList();

        initHandlerByMethods(declaredMethods);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }

    private void initHandlerByMethods(List<Method> declaredMethods) {
        for (Method method : declaredMethods) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            String url = mapping.value();
            RequestMethod[] requestMethods = getRequestMethods(mapping, mapping.method());
            putHandlerKeyAndExecution(method, requestMethods, url);
        }
    }

    private void putHandlerKeyAndExecution(Method method, RequestMethod[] requestMethods, String url) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = createHandlerExecution(method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping mapping, RequestMethod[] methods) {
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return mapping.method();
    }

    private HandlerExecution createHandlerExecution(Method method) {
        try {
            if (!cache.containsKey(method)) {
                cache.put(method, method.getDeclaringClass().getConstructor().newInstance());
            }
            return new HandlerExecution(cache.get(method), method);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
