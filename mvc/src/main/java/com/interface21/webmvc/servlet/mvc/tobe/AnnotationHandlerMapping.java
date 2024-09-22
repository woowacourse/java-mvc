package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        Controllers controllers = new Controllers(basePackage);
        Set<Method> methods = controllers.getAnnotationMethods(RequestMapping.class);
        for (Method method : methods) {
            Object controller = controllers.getController(method.getDeclaringClass());
            addHandlers(controller, method);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlers(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(url, requestMethod);
            HandlerExecution execution = new HandlerExecution(controller, method);
            addHandler(key, execution);
        }
    }

    private void addHandler(HandlerKey key, HandlerExecution execution) {
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalArgumentException("이미 등록된 URL과 HTTP 메서드 조합입니다: " + key);
        }
        handlerExecutions.put(key, execution);
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(key);
    }
}
