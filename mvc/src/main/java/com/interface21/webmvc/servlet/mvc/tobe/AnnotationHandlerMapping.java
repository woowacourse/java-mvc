package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var controllers = new ControllerScanner(basePackage).getControllers();
        for (Method method : getRequestMappingMethods(controllers.keySet())) {
            addHandlerExecutions(controllers, method, method.getAnnotation(RequestMapping.class));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers, final Method method,
                                     final RequestMapping requestMapping) {
        final var handlerExecution = new HandlerExecution(controllers.get(method.getDeclaringClass()), method);
        for (HandlerKey handlerKey : mapHandlerKeys(requestMapping.value(), requestMapping.method())) {
            if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                throw new IllegalStateException(
                        "중복된 요청 매핑: " + handlerKey + ", 추가하려는 메서드: " + method);
            }
        }
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> controllerClasses) {
        final Set<Method> methods = new HashSet<>();
        for (Class<?> controllerClass : controllerClasses) {
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    private List<HandlerKey> mapHandlerKeys(final String url, final RequestMethod[] requestMethods) {
        var methods = requestMethods;
        if (methods.length == 0) {
            methods = RequestMethod.values();
        }

        return Arrays.stream(methods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }
}
