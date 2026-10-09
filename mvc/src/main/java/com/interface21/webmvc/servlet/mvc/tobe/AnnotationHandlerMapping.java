package com.interface21.webmvc.servlet.mvc.tobe;

import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final ControllerScanner controllerScanner) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = controllerScanner;
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        for (Map.Entry<Class<?>, Object> entry : controllerScanner.getControllers().entrySet()) {
            registerControllerHandlers(entry);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private void registerControllerHandlers(Map.Entry<Class<?>, Object> entry) {
        Set<Method> methods = ReflectionUtils.getAllMethods(entry.getKey(),
                withAnnotation(RequestMapping.class));

        Object instance = entry.getValue();
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String value = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();

            registerHandlerExecutions(requestMethods, value, instance, method);
        }
    }

    private void registerHandlerExecutions(RequestMethod[] requestMethods, String value, Object instance,
                                           Method method) {
        if (requestMethods.length < 1) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            validateDuplicated(handlerKey);
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private void validateDuplicated(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("RequestMapping이 중복되어 특정할 수 없습니다.: " + handlerKey);
        }
    }
}
