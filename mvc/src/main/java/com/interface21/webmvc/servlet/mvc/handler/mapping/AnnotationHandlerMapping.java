package com.interface21.webmvc.servlet.mvc.handler.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.handler.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
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
        try {
            final Reflections reflections = new Reflections(basePackage);
            final ControllerScanner controllerScanner = new ControllerScanner(reflections);
            final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
            initHandlerExecutions(controllers);
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (final Exception e) {
            log.error("Failed to initialize AnnotationHandlerMapping");
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod method = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), method);

        return handlerExecutions.get(handlerKey);
    }

    private void initHandlerExecutions(final Map<Class<?>, Object> controllers) {
        for (Class<?> controller : controllers.keySet()) {
            final Object handler = controllers.get(controller);
            final Set<Method> methods = ReflectionUtils.getAllMethods(controller,
                    ReflectionUtils.withAnnotation(RequestMapping.class));
            for (Method method : methods) {
                initHandlerExecutionByMethod(method, handler);
            }
        }
    }

    private void initHandlerExecutionByMethod(
            final Method method,
            final Object handler
    ) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(method, handler);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
