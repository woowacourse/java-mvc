package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        for (final Object basePackage : basePackages) {
            scanBasePackage(basePackage);
        }
    }

    private void scanBasePackage(Object basePackage) {
        Reflections reflections = new Reflections(basePackage);

        for (final Class<?> aClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            for (final Method method : aClass.getDeclaredMethods()) {
                registerHandler(aClass, method);
            }
        }
    }

    private void registerHandler(Class<?> aClass, Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null){
            return;
        }

        final String url = requestMapping.value();
        for (final RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(aClass, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod method = RequestMethod.from(request.getMethod());
        final String requestURI = request.getRequestURI();
        final HandlerKey handlerKey = new HandlerKey(requestURI, method);

        return handlerExecutions.get(handlerKey);
    }
}
