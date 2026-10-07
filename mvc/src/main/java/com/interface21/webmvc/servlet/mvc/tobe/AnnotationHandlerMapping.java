package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (final Object packageName : basePackage) {
            final Reflections reflections = new Reflections(String.valueOf(packageName));
            final Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

            for (final Class<?> controllerType : controllerTypes) {
                registerController(controllerType);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (final IllegalArgumentException exception) {
            return null;
        }

        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }

    private void registerController(final Class<?> controllerType) {
        final Object controller = createController(controllerType);
        final RequestMapping typeMapping = controllerType.getAnnotation(RequestMapping.class);
        final String typePath = typeMapping == null ? "" : typeMapping.value();

        for (final Method method : controllerType.getDeclaredMethods()) {
            final RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
            if (methodMapping == null) {
                continue;
            }

            final String url = combinePaths(typePath, methodMapping.value());
            for (final RequestMethod requestMethod : resolveRequestMethods(typeMapping, methodMapping)) {
                final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
            }
        }
    }

    private Object createController(final Class<?> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (final ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not create controller: " + controllerType.getName(), exception);
        }
    }

    private Set<RequestMethod> resolveRequestMethods(final RequestMapping typeMapping,
                                                     final RequestMapping methodMapping) {
        final RequestMethod[] typeMethods = typeMapping == null ? new RequestMethod[0] : typeMapping.method();
        final RequestMethod[] methodMethods = methodMapping.method();

        if (typeMethods.length == 0 && methodMethods.length == 0) {
            return EnumSet.allOf(RequestMethod.class);
        }
        if (typeMethods.length == 0) {
            return EnumSet.copyOf(Arrays.asList(methodMethods));
        }
        if (methodMethods.length == 0) {
            return EnumSet.copyOf(Arrays.asList(typeMethods));
        }

        final Set<RequestMethod> resolvedMethods = EnumSet.noneOf(RequestMethod.class);
        for (final RequestMethod method : methodMethods) {
            if (Arrays.asList(typeMethods).contains(method)) {
                resolvedMethods.add(method);
            }
        }
        return resolvedMethods;
    }

    private String combinePaths(final String typePath, final String methodPath) {
        if (typePath.isEmpty()) {
            return methodPath;
        }
        if (methodPath.isEmpty()) {
            return typePath;
        }

        final String normalizedTypePath = typePath.endsWith("/")
                ? typePath.substring(0, typePath.length() - 1)
                : typePath;
        final String normalizedMethodPath = methodPath.startsWith("/")
                ? methodPath.substring(1)
                : methodPath;
        return normalizedTypePath + "/" + normalizedMethodPath;
    }
}
