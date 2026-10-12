package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Map.of();

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        final Map<HandlerKey, HandlerExecution> mappings = new HashMap<>();
        for (final var entry : controllerScanner.scan().entrySet()) {
            final Class<?> controllerClass = entry.getKey();
            for (final Method method : findHandlerMethods(controllerClass)) {
                final var handler = new HandlerExecution(entry.getValue(), method);
                final var mapping = findRequestMapping(controllerClass, method);
                for (final RequestMethod requestMethod : resolveRequestMethods(mapping)) {
                    final var key = new HandlerKey(mapping.value(), requestMethod);
                    if (mappings.putIfAbsent(key, handler) != null) {
                        throw new IllegalStateException("Duplicate request mapping: " + key);
                    }
                    log.info("Mapped {} to {}.{}", key, controllerClass.getSimpleName(), method.getName());
                }
            }
        }
        handlerExecutions = Map.copyOf(mappings);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        final var requestPath = request.getRequestURI().substring(request.getContextPath().length());
        return handlerExecutions.get(new HandlerKey(requestPath, requestMethod));
    }

    private Set<Method> findHandlerMethods(final Class<?> controllerClass) {
        final Set<Method> annotatedMethods = ReflectionUtils.getAllMethods(
                controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
        final Set<Method> methods = new HashSet<>();
        for (final Method method : annotatedMethods) {
            methods.add(mostSpecificMethod(controllerClass, method));
        }
        return methods;
    }

    private Method mostSpecificMethod(final Class<?> controllerClass, final Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return method;
        }
        try {
            return controllerClass.getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return method;
        }
    }

    private RequestMapping findRequestMapping(final Class<?> controllerClass, final Method method) {
        for (Class<?> type = controllerClass; type != null; type = type.getSuperclass()) {
            final var mapping = declaredMapping(type, method);
            if (mapping != null) {
                return mapping;
            }
        }
        final Map<Class<?>, RequestMapping> interfaces = new HashMap<>();
        collectInterfaceMappings(controllerClass, method, interfaces);
        RequestMapping resolved = null;
        for (final var entry : interfaces.entrySet()) {
            final boolean overridden = interfaces.keySet().stream()
                    .anyMatch(type -> type != entry.getKey() && entry.getKey().isAssignableFrom(type));
            if (overridden) {
                continue;
            }
            if (resolved != null && !sameMapping(resolved, entry.getValue())) {
                throw new IllegalStateException("Ambiguous request mapping: " + method);
            }
            resolved = entry.getValue();
        }
        return resolved;
    }

    private RequestMapping declaredMapping(final Class<?> type, final Method method) {
        try {
            return type.getDeclaredMethod(method.getName(), method.getParameterTypes())
                    .getAnnotation(RequestMapping.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private void collectInterfaceMappings(final Class<?> type, final Method method,
                                          final Map<Class<?>, RequestMapping> mappings) {
        if (type == null) {
            return;
        }
        if (type.isInterface()) {
            final var mapping = declaredMapping(type, method);
            if (mapping != null) {
                mappings.put(type, mapping);
            }
        }
        for (final Class<?> implemented : type.getInterfaces()) {
            collectInterfaceMappings(implemented, method, mappings);
        }
        collectInterfaceMappings(type.getSuperclass(), method, mappings);
    }

    private boolean sameMapping(final RequestMapping first, final RequestMapping second) {
        return first.value().equals(second.value())
                && resolveRequestMethods(first).equals(resolveRequestMethods(second));
    }

    private Set<RequestMethod> resolveRequestMethods(final RequestMapping mapping) {
        if (mapping.method().length == 0) {
            return EnumSet.allOf(RequestMethod.class);
        }
        final var methods = EnumSet.noneOf(RequestMethod.class);
        methods.addAll(Arrays.asList(mapping.method()));
        return methods;
    }
}
