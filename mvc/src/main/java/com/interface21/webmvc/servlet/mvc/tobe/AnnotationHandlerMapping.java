package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final Map<String, HandlerExecution> defaultHandlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.defaultHandlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Arrays.stream(basePackage).forEach(pkg -> {
            List<Object> controllers = ControllerScanner.scan(pkg);
            controllers.forEach(controller -> {
                Arrays.stream(controller.getClass().getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .forEach(method -> addHandler(controller, method));
            });
            log.info("Controllers : {}", controllers);
        });
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.info("Request URI : {}", requestURI);

        String method = request.getMethod();
        log.info("Request Method : {}", method);
        HandlerExecution handlerExecution = handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
        if (handlerExecution != null) {
            return handlerExecution;
        }
        return defaultHandlerExecutions.get(requestURI);
    }

    private void addHandler(final Object controller, final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        log.info("value: {} -> method: {}", annotation.value(), annotation.method());

        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        if (annotation.method().length == 0) {
            if (defaultHandlerExecutions.putIfAbsent(annotation.value(), handlerExecution) != null) {
                throw new IllegalStateException("중복된 요청 매핑입니다: " + annotation.value()
                        + " (HTTP 메서드 생략)");
            }
            return;
        }

        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                throw new IllegalStateException("중복된 요청 매핑입니다: " + handlerKey);
            }
        }
    }
}
