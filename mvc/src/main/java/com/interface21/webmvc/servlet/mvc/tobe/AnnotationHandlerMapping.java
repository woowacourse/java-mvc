package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Entry<Class<?>, Object> controllerMap : controllers.entrySet()) {
            final Class<?> controllerClass = controllerMap.getKey();
            final Object controllerInstance = controllerMap.getValue();
            final List<Method> requestMappingMethods = getRequestMappingMethods(controllerClass);
            for (Method requestMappingMethod : requestMappingMethods) {
                final var handlerExecution = new HandlerExecution(requestMappingMethod, controllerInstance);
                putHandlerExecutions(requestMappingMethod, handlerExecution);
            }
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

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final var handlerKey = convertRequestToHandlerKey(request);
        if (!handlerExecutions.containsKey(handlerKey)) {
            return null;
        }
        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey convertRequestToHandlerKey(final HttpServletRequest request) {
        final var requestMethod = RequestMethod.getByName(request.getMethod());
        final var url = request.getRequestURI();
        return new HandlerKey(url, requestMethod);
    }
}
