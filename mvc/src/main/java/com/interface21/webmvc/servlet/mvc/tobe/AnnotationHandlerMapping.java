package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

public class AnnotationHandlerMapping {

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
            for (final Method method : getAllMethods(entry.getKey(), withAnnotation(RequestMapping.class))) {
                registerHandler(entry.getValue(), method);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandler(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        for (final RequestMethod requestMethod : getRequestMethods(requestMapping)) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                throw new IllegalStateException("중복된 요청 매핑입니다: " + requestMapping.value() + " " + requestMethod);
            }
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }
}
