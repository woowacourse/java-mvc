package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(controllerClass -> {
            Object controllerInstance = toInstance(controllerClass);
            registerHandler(controllerInstance);
        });
    }

    private Object toInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor()
                .newInstance();
        } catch (Exception e) {
            log.info("인스턴스 {} 생성 실패: {}", clazz.getName(), e.getMessage());
            throw new RuntimeException("인스턴스 생성에 실패했습니다: " + clazz, e);
        }
    }

    private void registerHandler(final Object handler) {
        Method[] declaredMethods = handler.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerRequestMappingMethod(handler, method);
            }
        }
    }

    private void registerRequestMappingMethod(final Object handler, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(uri, requestMethod);
            HandlerExecution execution = new HandlerExecution(handler, method);
            handlerExecutions.put(key, execution);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        HandlerKey key = new HandlerKey(uri, requestMethod);

        return handlerExecutions.get(key);
    }
}
