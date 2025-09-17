package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            final Class<?> controllerClass = entry.getKey();
            final Object controllerInstance = entry.getValue();
            registerRequestMappings(controllerInstance, controllerClass);
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }

    private void registerRequestMappings(Object controllerInstance, Class<?> controllerClass) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            for (RequestMethod httpMethod : mapping.method()) {
                HandlerKey key = new HandlerKey(mapping.value(), httpMethod);
                if (handlerExecutions.containsKey(key)) {
                    throw new IllegalStateException("중복 매핑");
                }
                handlerExecutions.put(key, new HandlerExecution(controllerInstance, method));
            }
        }
    }
}
