package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RequestMappingMethod {
    
    private final Method method;
    private final RequestMapping requestMapping;

    public RequestMappingMethod(Method method) {
        validateMethod(method);
        this.method = method;
        this.requestMapping = method.getAnnotation(RequestMapping.class);
    }

    private static void validateMethod(Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            throw new IllegalArgumentException("Method must be annotated with @RequestMapping");
        }
    }

    public List<Handler> createHandlers(Object controller) {
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        return createHandlerKeys().stream()
                .map(handlerKey -> new Handler(handlerKey, handlerExecution))
                .toList();
    }

    private List<HandlerKey> createHandlerKeys() {
        return Arrays.stream(getRequestMethods())
                .map(this::createHandlerKey)
                .toList();
    }

    private HandlerKey createHandlerKey(RequestMethod requestMethod) {
        String url = requestMapping.value();
        return new HandlerKey(url, requestMethod);
    }

    private RequestMethod[] getRequestMethods() {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }
}
