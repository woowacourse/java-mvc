package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            registerController(entry.getKey(), entry.getValue());
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(final Class<?> controllerClass, final Object controller) {
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandler(controller, method);
            }
        }
    }

    private void registerHandler(final Object controller, final Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        log.info("URL: {}, HTTP methods: {}",
                mapping.value(),
                Arrays.toString(mapping.method()));

        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        RequestMethod[] requestMethods = mapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
            if (handlerExecutions.containsKey(key)) {
                throw new IllegalArgumentException("중복된 요청 매핑: " + key);
            }
            handlerExecutions.put(key, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(key);
    }
}
