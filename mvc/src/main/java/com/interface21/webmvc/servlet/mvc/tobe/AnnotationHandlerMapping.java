package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ReflectionUtilsPredicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final ControllerScanner scanner = new ControllerScanner(reflections);
        final Map<Class<?>, Object> controllers = scanner.getController();

        for (final Entry<Class<?>, Object> classObjectEntry : controllers.entrySet()) {
            addAllHandler(classObjectEntry.getKey(), classObjectEntry.getValue());
        }

        log.info("init handlerExecutions: {}", handlerExecutions);
    }

    private void addAllHandler(final Class<?> aClass, final Object controller) {
        final Set<Method> allMethods = ReflectionUtils.getAllMethods(aClass,
                ReflectionUtilsPredicates.withAnnotation(RequestMapping.class));
        for (final Method method : allMethods) {
            addHandler(controller, method);
        }
    }

    private void addHandler(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        for (final RequestMethod requestMethod : getRequestMethods(method)) {
            final HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
            validateDuplicate(key);
            handlerExecutions.put(key, new HandlerExecution(controller, method));
        }
    }

    private RequestMethod[] getRequestMethods(final Method declaredMethod) {
        final RequestMethod[] requestMethods = declaredMethod.getAnnotation(RequestMapping.class).method();

        if (requestMethods.length != 0) {
            return requestMethods;
        }
        return RequestMethod.values();
    }

    private void validateDuplicate(final HandlerKey key) {
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalStateException("중복된 매핑 요청입니다.");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.getByValue(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
