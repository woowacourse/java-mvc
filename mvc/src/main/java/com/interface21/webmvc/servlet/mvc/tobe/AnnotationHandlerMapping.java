package com.interface21.webmvc.servlet.mvc.tobe;

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

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final ControllerScanner controllerScanner, final Object... basePackage) {
        this.controllerScanner = controllerScanner;
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.scan(basePackage);
        controllers.forEach(this::registerHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))
        );
    }

    private void registerHandlerExecutions(final Class<?> controllerClass, final Object controllerInstance) {
        Set<Method> methods = ReflectionUtils.getAllMethods(
                controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );

        for (Method method : methods) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            for (RequestMethod requestMethod : mapping.method()) {
                handlerExecutions.put(
                        new HandlerKey(mapping.value(), requestMethod),
                        new HandlerExecution(controllerInstance, method)
                );
            }
        }
    }
}
