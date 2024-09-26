package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;


    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(doMap());

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Consumer<Class<?>> doMap() {
        return clazz -> initHandlerExecutions(clazz, Arrays.stream(clazz.getDeclaredMethods()).toList());
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
            handlerExecutions.put(url, requestMethod, controllerClazz, method);
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping mapping, RequestMethod[] methods) {
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return mapping.method();
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }
}
