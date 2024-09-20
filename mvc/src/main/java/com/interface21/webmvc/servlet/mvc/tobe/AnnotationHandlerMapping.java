package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
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
        if (isInvalidBasePackage()) {
            log.warn("base 패키지가 유효하지 않습니다.");
            return;
        }
        Arrays.stream(basePackage)
                .forEach(this::initializeHandlers);
        log.info("AnnotationHandlerMapping 초기화 완료");
    }

    private boolean isInvalidBasePackage() {
        return basePackage == null || basePackage.length == 0;
    }

    private void initializeHandlers(Object packageObject) {
        Reflections reflections = new Reflections(packageObject);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        controllerClasses
                .forEach(this::initializeHandler);
    }

    private void initializeHandler(Class<?> controllerClass) {
        Arrays.stream(controllerClass.getMethods())
                .filter(this::isRequestMappingAnnotated)
                .forEach(method -> registerHandler(controllerClass, method));
    }

    private boolean isRequestMappingAnnotated(Method method) {
        return method.getAnnotation(RequestMapping.class) != null;
    }

    private void registerHandler(Class<?> controllerClass, Method method) {
        Object controllerInstance = createControllerInstance(controllerClass);
        if (controllerInstance == null) {
            return;
        }

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = extractRequestMethods(requestMapping);

        Arrays.stream(requestMethods)
                .forEach(requestMethod -> {
                    HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                    handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
                });
    }

    private Object createControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("생성자를 호출할 수 없습니다. " + controllerClass.getName(), e);
            return null;
        }
    }

    private RequestMethod[] extractRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods == null || requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.findByName(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
