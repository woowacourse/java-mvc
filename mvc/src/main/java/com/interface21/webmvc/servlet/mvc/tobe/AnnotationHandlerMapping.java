package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = initialize(controllerScanner.scan());
    }

    private Map<HandlerKey, HandlerExecution> initialize(Map<Class<?>, Object> controllerByClasses) {
        log.info("Initialized AnnotationHandlerMapping!");

        Map<HandlerKey, HandlerExecution> mapping = new HashMap<>();
        for (Map.Entry<Class<?>, Object> entry : controllerByClasses.entrySet()) {
            Class<?> controllerClass = entry.getKey();
            Object controller = entry.getValue();

            RequestMapping classMapping = controllerClass.getAnnotation(RequestMapping.class);
            String classPath = getPath(classMapping);

            Set<Method> allMethods = ReflectionUtils.getAllMethods(
                    controllerClass,
                    ReflectionUtils.withAnnotation(RequestMapping.class)
            );

            for (Method method : allMethods) {
                RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
                if (methodMapping == null) {
                    continue;
                }

                String methodPath = getPath(methodMapping);
                RequestMethod[] requestMethods = getRequestMethods(methodMapping);

                String fullPath = normalize(classPath, methodPath);
                for (RequestMethod httpMethod : requestMethods) {
                    HandlerKey key = new HandlerKey(fullPath, httpMethod);
                    HandlerExecution prev = mapping.putIfAbsent(key, new HandlerExecution(controller, method));
                    if (prev != null) {
                        throw new IllegalStateException("Duplicate handler mapping detected: " + key);
                    }
                }
            }
        }
        return Collections.unmodifiableMap(mapping);
    }

    private String getPath(RequestMapping mapping) {
        if (mapping == null) {
            return "";
        }
        String path = mapping.value();
        if (path == null || path.isEmpty()) {
            return "";
        }
        return path;
    }

    private String normalize(String base, String path) {
        String normalizedBase = (base == null) ? "" : base.trim();
        String normalizePath = (path == null) ? "" : path.trim();
        if (!normalizedBase.startsWith("/")) {
            normalizedBase = "/" + normalizedBase;
        }
        if (normalizedBase.endsWith("/")) {
            normalizedBase = normalizedBase.substring(0, normalizedBase.length() - 1);
        }
        if (!normalizePath.startsWith("/")) {
            normalizePath = "/" + normalizePath;
        }
        String joined = (normalizedBase + normalizePath).replaceAll("//+", "/");
        return joined.isEmpty() ? "/" : joined;
    }

    private RequestMethod[] getRequestMethods(RequestMapping methodMapping) {
        RequestMethod[] requestMethods = methodMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.findByName(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
