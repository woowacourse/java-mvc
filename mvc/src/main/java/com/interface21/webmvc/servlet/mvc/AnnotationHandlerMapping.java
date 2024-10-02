package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::processController);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.of(request.getMethod()));

        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("No handler found for request uri: " + request.getRequestURI());
        }

        return handlerExecutions.get(handlerKey);
    }

    private void processController(Class<?> controller, Object instance) {
        List<Method> annotatedMethods = Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

        for (Method method : annotatedMethods) {
            registerHandlerExecution(instance, method);
        }
    }

    private void registerHandlerExecution(Object controllerInstance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = findRequestMethods(requestMapping);

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            checkAlreadyHasHandlerKey(handlerKey);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    protected void checkAlreadyHasHandlerKey(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("동일한 URL에 대해 같은 HTTP 메서드를 사용할 수 없습니다.");
        }
    }

    private RequestMethod[] findRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }

        return requestMethods;
    }
}
