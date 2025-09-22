package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        handlerMapping();
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void handlerMapping() {
        Reflections reflections = new Reflections(basePackage);
        var controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllers) {
            try {
                Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                for (var method : controllerClass.getDeclaredMethods()) {
                    addHandlerExecutions(method, controllerInstance);
                }
            } catch (Exception e) {
                log.error("컨트롤러 인스턴스 생성 실패: {}", controllerClass.getName(), e);
            }
        }
    }

    private void addHandlerExecutions(Method method, Object controllerInstance) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            for (RequestMethod requestMethod : requestMapping.method()) {
                handlerExecutions.put(
                    new HandlerKey(requestMapping.value(), requestMethod),
                    new HandlerExecution(controllerInstance, method)
                );
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestUri, RequestMethod.from(method));
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new IllegalArgumentException("잘못된 요청입니다: " + requestUri);
        }
        return handlerExecution;
    }
}
