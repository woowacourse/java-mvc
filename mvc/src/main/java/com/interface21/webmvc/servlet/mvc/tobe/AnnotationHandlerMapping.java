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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controllerClass : controllerClasses) {
            try {
                final Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                final Method[] methods = controllerClass.getDeclaredMethods();
                for (final Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        final String url = requestMapping.value();
                        final RequestMethod[] httpMethods = requestMapping.method();
                        final HandlerExecution handler = new HandlerExecution(controllerInstance, method);

                        if (httpMethods.length == 0) {
                            for (final RequestMethod requestMethod : RequestMethod.values()) {
                                handlerExecutions.put(new HandlerKey(url, requestMethod), handler);
                            }
                        } else {
                            for (final RequestMethod requestMethod : httpMethods) {
                                handlerExecutions.put(new HandlerKey(url, requestMethod), handler);
                            }
                        }
                    }
                }
            } catch (final Exception e) {
                log.error("Failed to initialize handler mapping for controller {}", controllerClass.getName(), e);
                throw new RuntimeException(e);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey key = new HandlerKey(requestUri, requestMethod);
        return handlerExecutions.get(key);
    }
}
