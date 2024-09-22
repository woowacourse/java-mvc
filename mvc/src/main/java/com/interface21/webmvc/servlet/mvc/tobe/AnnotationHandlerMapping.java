package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
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
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class).stream()
                .flatMap(clazz -> Arrays.stream(clazz.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::addHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlers(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            addHandler(method, url, requestMethod);
        }
    }

    private void addHandler(Method method, String url, RequestMethod requestMethod) {
        HandlerKey key = new HandlerKey(url, requestMethod);
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalArgumentException("이미 등록된 URL과 HTTP 메서드 조합입니다: " + requestMethod + " " + url);
        }
        HandlerExecution execution = new HandlerExecution(method.getDeclaringClass(), method);
        handlerExecutions.put(key, execution);
    }

    public Object getHandler(final HttpServletRequest request) {
        return null;
    }
}
