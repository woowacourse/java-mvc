package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        final var samples = new Reflections(basePackage);
        try {
            final Set<Class<?>> controllerClasses = samples.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controllerClass : controllerClasses) {
                final List<Method> requestMappingMethods = getRequestMappingMethods(controllerClass);
                final Object targetClass = controllerClass.getConstructor().newInstance();
                for (Method requestMappingMethod : requestMappingMethods) {
                    final var handlerExecution = new HandlerExecution(requestMappingMethod, targetClass);
                    putHandlerExecutions(requestMappingMethod, handlerExecution);
                }
            }
        } catch (Exception e) {
            log.error("Handler 매핑 중 에러가 발생했습니다.", e.fillInStackTrace());
        }
    }

    private List<Method> getRequestMappingMethods(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void putHandlerExecutions(final Method requestMappingMethod, final HandlerExecution handlerExecution) {
        final RequestMapping annotation = requestMappingMethod.getAnnotation(RequestMapping.class);
        final String url = annotation.value();
        final RequestMethod[] httpMethods = getAnnotationHttpMethods(annotation.method());
        for (RequestMethod httpMethod : httpMethods) {
            final var handlerKey = new HandlerKey(url, httpMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] getAnnotationHttpMethods(final RequestMethod[] methods) {
        if (methods.length == 0) {
            return RequestMethod.allValues();
        }
        return methods;
    }

    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = convertRequestToHandlerKey(request);
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("잘못된 요청입니다. : " + handlerKey);
        }
        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey convertRequestToHandlerKey(HttpServletRequest request) {
        final var requestMethod = RequestMethod.getByName(request.getMethod());
        final var url = request.getRequestURI();
        return new HandlerKey(url, requestMethod);
    }
}
