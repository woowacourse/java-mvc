package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        mapControllers(controllerScanner.getControllers());

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void mapControllers(final Map<Class<?>, Object> controllers) {
        for (Map.Entry<Class<?>, Object> entrySet : controllers.entrySet()) {
            mapControllerMethods(entrySet.getKey(), entrySet.getValue());
        }
    }

    private void mapControllerMethods(final Class<?> controllerClass, final Object controller) {
        List<Method> mappedMethods = Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

        for (Method method : mappedMethods) {
            try {
                addHandlerExecutions(controller, method);
            } catch (Exception e) {
                log.error("Failed to add handler execution.", e);
                throw new RuntimeException(e);
            }
        }
    }

    private void addHandlerExecutions(final Object controller, final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String url = annotation.value();
        RequestMethod[] requestMethods = annotation.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            addExecution(handlerKey, handlerExecution);
        }
    }

    private void addExecution(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("Mapping already exists: " + handlerKey.toString());
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }
}
