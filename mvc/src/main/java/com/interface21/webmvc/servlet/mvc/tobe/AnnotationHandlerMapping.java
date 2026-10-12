package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        final Map<Class<?>, Object> controllers = new ControllerScanner(basePackage).getControllers();
        for (final Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            for (final Method method : entry.getKey().getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    register(entry.getValue(), method);
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void register(final Object controller, final Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        String url = mapping.value();
        RequestMethod[] requestMethods = mapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("중복된 매핑입니다. " + handlerKey);
            }
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
