package com.interface21.webmvc.servlet.mvc.tobe.handler.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
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

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize(final ControllerScanner scanner) {
        Map<Class<?>, Object> controllers = scanner.getControllers(this.basePackage);
        for (Class<?> controller : controllers.keySet()) {

            Set<Method> methods = ReflectionUtils.getAllMethods(
                    controller,
                ReflectionUtils.withAnnotation(RequestMapping.class)
            );

            for (Method method : methods) {
                registerHandlerExecutions(method, controllers.get(controller));
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerExecutions(Method method, Object handler) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handler, method);
            handlerExecutions.put(handlerKey, handlerExecution);

            log.info("{}: {}", handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

        return handlerExecutions.get(handlerKey);
    }
}
