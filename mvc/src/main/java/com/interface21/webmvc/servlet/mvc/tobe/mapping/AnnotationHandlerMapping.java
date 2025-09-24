package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.execution.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.exception.MethodNotAllowedException;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final ControllerScanner controllerScanner = new ControllerScanner(basePackages);
        final Map<Class<?>, Object> controllers = controllerScanner.scan();

        controllers.forEach(this::registerHandlerMethods);
    }

    private void registerHandlerMethods(Class<?> clazz, Object controller) {
        getRequestMappingMethods(clazz).forEach(method -> {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final RequestMethod[] requestMethods = resolveRequestMethods(requestMapping);

            for (RequestMethod requestMethod : requestMethods) {
                registerHandler(controller, method, requestMapping.value(), requestMethod);
            }
        });
    }

    private Set<Method> getRequestMappingMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return methods;
    }

    private void registerHandler(Object controller, Method method, String url, RequestMethod requestMethod) {
        final var handlerKey = new HandlerKey(url, requestMethod);

        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("Duplicate mapping found: " + handlerKey);
        }

        handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        log.info("Mapping {} {}", requestMethod, url);
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        final var exactHandlerKey = new HandlerKey(requestURI, requestMethod);

        if (handlerExecutions.containsKey(exactHandlerKey)) {
            return handlerExecutions.get(exactHandlerKey);
        }

        final Set<String> allowedMethods = handlerExecutions.keySet()
                .stream()
                .filter(key -> key.isSameUrl(requestURI))
                .map(key -> key.getRequestMethod().name())
                .collect(Collectors.toSet());

        if (!allowedMethods.isEmpty()) {
            throw new MethodNotAllowedException(requestURI, allowedMethods);
        }

        return null;
    }
}
