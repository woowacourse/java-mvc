package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.core.ControllerScanner;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        controllerScanner.scan().forEach(this::addHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlers(final Class<?> controllerClass, final Object controllerObject) {
        ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class))
                        .forEach(controllerMethod -> addHandler(controllerObject, controllerMethod));
    }

    private void addHandler(final Object controllerObject, final Method controllerMethod) {
        final RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);
        RequestMethod[] requestHttpMethods = requestMapping.method();

        if (requestHttpMethods.length == 0) {
            requestHttpMethods = RequestMethod.values();
        }

        Arrays.stream(requestHttpMethods)
                .map(requestHttpMethod -> new HandlerKey(requestMapping.value(), requestHttpMethod))
                .forEach(handlerKey -> {
                    final HandlerExecution handlerExecution = new HandlerExecution(controllerObject, controllerMethod);
                    if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                        throw new IllegalStateException("이미 등록된 핸들러입니다: " + handlerKey);
                    }
                });
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())));
    }
}
