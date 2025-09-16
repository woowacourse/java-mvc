package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        requireBasePackages(basePackages);

        try {
            final Reflections reflections = new Reflections(this.basePackages);
            final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

            registerControllers(controllerClasses);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void requireBasePackages(final Object[] basePackages) {
        if (basePackages == null || basePackages.length == 0) {
            throw new IllegalArgumentException("backPackage가 설정되어야 합니다");
        }
    }

    private void registerControllers(Set<Class<?>> controllerClasses)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Class<?> controllerClass : controllerClasses) {
            final Object handler = controllerClass.getConstructor().newInstance();
            final Method[] methods = controllerClass.getMethods();

            scanHandlerMethods(methods, handler);
        }
    }

    private void scanHandlerMethods(final Method[] methods, final Object handler) {
        for (Method method : methods) {
            registerRequestMapping(handler, method);
        }
    }

    private void registerRequestMapping(final Object handler, final Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final List<HandlerKey> handlerKeys = createHandlerKeys(requestMapping);

            for (HandlerKey handlerKey : handlerKeys) {
                final HandlerExecution handlerExecution = new HandlerExecution(handler, method);

                registerHandler(handlerKey, handlerExecution);
            }
        }
    }

    private List<HandlerKey> createHandlerKeys(final RequestMapping requestMapping) {
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();

        final RequestMethod[] targetMethods = getRequestMethods(requestMethods);

        return Arrays.stream(targetMethods)
                .map(targetMethod -> new HandlerKey(url, targetMethod))
                .toList();
    }

    private void registerHandler(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("같은 RequestMapping url에 대한 중복 매핑이 불가능합니다");
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private RequestMethod[] getRequestMethods(final RequestMethod[] requestMethods) {
        RequestMethod[] targetMethods = requestMethods;

        if (requestMethods.length == 0) {
            targetMethods = RequestMethod.values();
        }
        return targetMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
