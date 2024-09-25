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
    private final HashMap<Class<?>, Object> cache = new HashMap<>();


    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(clazz -> initHandlerExecutions(clazz, Arrays.stream(clazz.getDeclaredMethods()).toList()));

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }

    private void initHandlerExecutions(Class<?> controllerClazz, List<Method> methods) {
        for (Method method : methods) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            String url = mapping.value();
            RequestMethod[] requestMethods = getRequestMethods(mapping, mapping.method());
            putHandlerKeyAndExecution(controllerClazz, method, requestMethods, url);
        }
    }

    private void putHandlerKeyAndExecution(
            Class<?> controllerClazz,
            Method method,
            RequestMethod[] requestMethods,
            String url
    ) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = createHandlerExecution(controllerClazz, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping mapping, RequestMethod[] methods) {
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return mapping.method();
    }

    private HandlerExecution createHandlerExecution(Class<?> controllerClazz, Method method) {
        try {
            if (!cache.containsKey(controllerClazz)) {
                cache.put(controllerClazz, controllerClazz.getConstructor().newInstance());
            }
            return new HandlerExecution(cache.get(controllerClazz), method);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
