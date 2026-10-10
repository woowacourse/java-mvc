package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.scanner.ControllerScanner;
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

        final Map<Class<?>, Object> controllers = ControllerScanner.getControllers(basePackage);

        for (Map.Entry<Class<?>, Object> controllerEntry : controllers.entrySet()) {
            Method[] methods = controllerEntry.getKey().getDeclaredMethods();
            putHandlerExecutions(controllerEntry.getValue(), methods);
        }
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerExecution handlerExecution = handlerExecutions.get(
            new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));

        if (handlerExecution == null) {
            return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.ANY));
        }

        return handlerExecution;
    }

    private void putHandlerExecutions(Object controller, Method[] controllerDeclaredMethods) {
        for (Method controllerMethod : controllerDeclaredMethods) {
            if (!controllerMethod.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            final RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);

            final HandlerExecution handlerExecution = new HandlerExecution(controller, controllerMethod);
            for (RequestMethod requestMethod : requestMethodsOf(requestMapping)) {
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);

                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private RequestMethod[] requestMethodsOf(RequestMapping requestMapping) {
        if (!existsRequestMethod(requestMapping)) {
            return new RequestMethod[]{RequestMethod.ANY};
        }

        return requestMapping.method();
    }

    private boolean existsRequestMethod(final RequestMapping requestMapping) {
        return requestMapping.method().length != 0;
    }

}
