package com.interface21.webmvc.servlet.mvc.tobe;

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
    private static final int EMPTY_REQUEST_METHOD_NUMBER = 0;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        addHandlerExecutions(controllers);
        log.info("AnnotationHandlerMapping initialized");
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers) {
        controllers.forEach((controllerClass, controllerInstance) ->
                Arrays.stream(controllerClass.getDeclaredMethods())
                        .forEach(this::registerHandlers)
        );
    }

    private void registerHandlers(Method method) {
        RequestMapping declaredAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
        String path = declaredAnnotation.value();
        RequestMethod[] requestMethods = declaredAnnotation.method();
        if (requestMethods.length == EMPTY_REQUEST_METHOD_NUMBER) {
            Arrays.stream(RequestMethod.values())
                    .forEach(requestMethod -> addHandler(path, requestMethod, method));
            return;
        }
        Arrays.stream(requestMethods)
                .forEach(requestMethod -> addHandler(path, requestMethod, method));
    }

    private void addHandler(String path, RequestMethod requestMethod, Method method) {
        HandlerKey handlerKey = new HandlerKey(path, requestMethod);
        handlerExecutions.put(handlerKey, new HandlerExecution(method));
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        String requestURI = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
