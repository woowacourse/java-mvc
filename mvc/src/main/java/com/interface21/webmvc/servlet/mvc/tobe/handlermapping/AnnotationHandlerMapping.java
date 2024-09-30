package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

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
        Set<Method> methods = controllerScanner.getAnnotationMethods(RequestMapping.class);
        for (Method method : methods) {
            Object controller = controllerScanner.getController(method.getDeclaringClass());
            addHandlers(controller, method);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlers(Object controller, Method method) {
        HandlerExecution execution = new HandlerExecution(controller, method);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
            addHandler(key, execution);
        }
    }

    private void addHandler(HandlerKey key, HandlerExecution execution) {
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalArgumentException("이미 등록된 URL과 HTTP 메서드 조합입니다: " + key);
        }
        handlerExecutions.put(key, execution);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(key);
    }
}
