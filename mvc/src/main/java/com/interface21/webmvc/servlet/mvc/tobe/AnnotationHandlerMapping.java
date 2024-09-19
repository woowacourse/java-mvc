package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final String DELIMITER_PATH = "/";
    private static final String REGEX_MANY_PATH_DELIMITER = "/{2,}";
    private static final String DEFAULT_PATH = "";

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class).forEach(this::assignHandlerByClass);
    }

    private void assignHandlerByClass(Class<?> clazz) {
        RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
        String path = Optional.ofNullable(annotation)
                .map(RequestMapping::value)
                .orElse(DEFAULT_PATH);

        for (Method method : clazz.getMethods()) {
            assignHandlerByMethod(clazz, method, path);
        }
    }

    private void assignHandlerByMethod(Class<?> clazz, Method method, String basePath) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        if (annotation == null) {
            return;
        }
        String subPath = Optional.of(annotation.value()).orElse(DEFAULT_PATH);
        String path = String.join(DELIMITER_PATH, DELIMITER_PATH, basePath, subPath)
                .replaceAll(REGEX_MANY_PATH_DELIMITER, DELIMITER_PATH);

        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            HandlerExecution handlerExecution = findHandlerExecution(clazz, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private HandlerExecution findHandlerExecution(Class<?> clazz, Method method) {
        try {
            return new HandlerExecution(method, clazz);
        } catch (Exception e) {
            log.error("Failed to find HandlerExecution for class {}", clazz, e);
            return null;
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            log.error("Failed to get HandlerExecution");
            return null;
        }

        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
