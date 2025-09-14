package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("AnnotationHandlerMapping 초기화 시작 - basePackage: {}", Arrays.toString(basePackage));
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controllerClass : controllerClasses) {
            registerHandlerExecution(controllerClass);
        }
        log.info("AnnotationHandlerMapping 초기화 완료 - 총 {}개의 핸들러 등록", handlerExecutions.size());
        if (log.isDebugEnabled()) {
            handlerExecutions.forEach((key, value) -> log.debug("등록된 핸들러 - {}", key));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        log.debug("요청 URI: {}, Method: {}", requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerHandlerExecution(final Class<?> controllerClass) {
        final Object controllerInstance = getControllerInstance(controllerClass);
        final List<Method> targetMethods = getTargetMethods(controllerClass);
        targetMethods.forEach(method -> addHandlerExecutions(controllerInstance, method));
    }

    private Object getControllerInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (final Exception exception) {
            log.error("Controller '{}' 인스턴스 생성 실패", controllerClass.getName(), exception);
            throw new IllegalArgumentException(exception);
        }
    }

    private List<Method> getTargetMethods(final Class<?> controllerClass) {
        return Stream.of(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addHandlerExecutions(
            final Object controllerInstance,
            final Method method
    ) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] effectiveMethods = getRequestMethods(requestMapping);
        for (final RequestMethod requestMethod : effectiveMethods) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }
}
