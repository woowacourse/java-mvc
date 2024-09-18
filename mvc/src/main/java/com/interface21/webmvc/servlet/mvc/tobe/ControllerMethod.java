package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ControllerMethod {
    
    private final Method controllerMethod;

    public ControllerMethod(Method controllerMethod) {
        validateMethod(controllerMethod);
        this.controllerMethod = controllerMethod;
    }

    private static void validateMethod(Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            throw new IllegalArgumentException("Method must be annotated with @RequestMapping");
        }
    }

    public List<Handler> createHandlers(Object controller) {
        HandlerExecution handlerExecution = new HandlerExecution(controller, controllerMethod);

        return createHandlerKeys().stream()
                .map(handlerKey -> new Handler(handlerKey, handlerExecution))
                .toList();
    }

    private List<HandlerKey> createHandlerKeys() {
        RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        String url = requestMapping.value();

        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }
}
