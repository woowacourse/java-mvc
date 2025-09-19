package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = initialize(basePackage);
    }

    public Map<HandlerKey, HandlerExecution> initialize(Object[] basePackage) {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        try {
            log.info("Initialized AnnotationHandlerMapping!");
            Reflections reflections = new Reflections(basePackage);

            for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
                String basePath = getBasePath(controllerClass);

                Object controller = controllerClass.getDeclaredConstructor().newInstance();

                for (Method method : controllerClass.getMethods()) {
                    RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                    if (methodRequestMapping == null) {
                        continue;
                    }

                    String fullPath = normalize(basePath, methodRequestMapping.value());
                    RequestMethod[] requestMethods = getRequestMethods(methodRequestMapping);
                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerExecution handlerExecution = handlerExecutions.putIfAbsent(new HandlerKey(fullPath, requestMethod), new HandlerExecution(controller, method));
                        if (handlerExecution != null) {
                            throw new IllegalStateException("Duplicate handler mapping detected");
                        }
                    }
                }
            }
            return Collections.unmodifiableMap(handlerExecutions);
        } catch (RuntimeException e) {
            throw new IllegalStateException("AnnotationHandlerMapping runtime failure = " + e);
        } catch (Exception e) {
            throw new IllegalStateException("annotationHandlerMapping initialization failure = " + e);
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping methodMapping) {
        RequestMethod[] requestMethods = methodMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    private String getBasePath(Class<?> controllerClass) {
        RequestMapping typeMapping = controllerClass.getAnnotation(RequestMapping.class);
        if (typeMapping == null) {
            return "";
        }
        return typeMapping.value();
    }

    private String normalize(String base, String path) {
        String a = (base == null) ? "" : base.trim();
        String b = (path == null) ? "" : path.trim();
        if (!a.startsWith("/")) {
            a = "/" + a;
        }
        if (a.endsWith("/")) {
            a = a.substring(0, a.length() - 1);
        }
        if (!b.startsWith("/")) {
            b = "/" + b;
        }
        return (a + b).replaceAll("//+", "/");
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.findByName(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
