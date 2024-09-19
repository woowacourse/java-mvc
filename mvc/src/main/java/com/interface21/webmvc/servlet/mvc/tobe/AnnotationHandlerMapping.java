package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);

        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            for (Method method : controllerClass.getDeclaredMethods()) {
                addHandlerExecution(controllerClass, method);
            }
        }
    }

    private void addHandlerExecution(Class<?> controllerClass, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String mappedUrl = requestMapping.value();
        RequestMethod requestMethod = requestMapping.method()[0];
        HandlerKey handlerKey = new HandlerKey(mappedUrl, requestMethod);

        handlerExecutions.put(handlerKey, new HandlerExecution(controllerClass, method));
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.of(request.getMethod()));
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new NoHandlerFoundException(
                    "[%s %s]에 매핑된 핸들러가 존재하지 않습니다.".formatted(request.getMethod(), request.getRequestURI())
            );
        }
        return handlerExecution;
    }
}
