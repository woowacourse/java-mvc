package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
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
        log.info("Initialized AnnotationHandlerMapping!");

        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> instances = controllerScanner.getControllers();
        instances.forEach(this::registerController);
    }

    private void registerController(Class<?> clazz, Object instance) {
        Method[] methods = clazz.getDeclaredMethods();
        Stream.of(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerHandlerMethod(method, instance));
    }

    private void registerHandlerMethod(Method method, Object instance) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(method, instance);
        String path = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return findHandler(requestURI, method);
    }

    private HandlerExecution findHandler(String requestURI, String method) {
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);

        if (handlerExecution == null) {
            return findHandlerWithoutMethod(requestURI);
        }
        return handlerExecution;
    }

    private HandlerExecution findHandlerWithoutMethod(String requestURI) {
        HandlerKey handlerKey = new HandlerKey(requestURI, null);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);

        if (handlerExecution == null) {
            throw new IllegalArgumentException("요청에 맞는 Handler를 찾을 수 없습니다");
        }
        return handlerExecution;
    }
}
