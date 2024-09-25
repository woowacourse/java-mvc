package com.techcourse;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner();
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers(basePackages);
        for (Entry<Class<?>, Object> classAndInstance : controllers.entrySet()) {
            Class<?> clazz = classAndInstance.getKey();
            Object instance = classAndInstance.getValue();

            initializeMethods(instance, clazz.getMethods());
        }
    }

    private void initializeMethods(Object controller, Method[] methods) {
        for (Method method : methods) {
            initializeMethod(controller, method);
        }
    }

    private void initializeMethod(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }

        String url = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        initializeHandlerExecution(controller, method, requestMethods, url);
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        return requestMethods;
    }

    private void initializeHandlerExecution(Object controller, Method method, RequestMethod[] requestMethods,
                                            String url) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new IllegalArgumentException("handlerKey에 매핑되는 handlerExecution이 존재하지 않습니다. (handlerKey: %s)"
                    .formatted(handlerKey));
        }
        return handlerExecution;
    }
}
