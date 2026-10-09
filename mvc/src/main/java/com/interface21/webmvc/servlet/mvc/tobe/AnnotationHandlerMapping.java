package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            registerController(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        RequestMethod requestMethod = null;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }

        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerController(final Class<?> controllerClass, final Object controller) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            registerHandlerMethod(controller, method);
        }
    }

    private void registerHandlerMethod(final Object controller, final Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(method, controller);

        for (RequestMethod requestMethod : getRequestMethods(requestMapping)) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            validateNotDuplicated(handlerKey);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    private void validateNotDuplicated(final HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("이미 등록된 핸들러 매핑입니다: " + handlerKey);
        }
    }
}
