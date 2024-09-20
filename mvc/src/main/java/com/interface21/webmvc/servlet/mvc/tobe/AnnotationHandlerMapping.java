package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.Arrays;
import java.util.Set;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Arrays.stream(basePackage)
                .flatMap(basePackage -> findControllers(basePackage).stream())
                .forEach(controller -> {
                    try {
                        handlerExecutions.registerController(controller);
                    } catch (Exception e) {
                        log.error("controller register 실패: {}", controller.getName(), e);
                    }
                });
    }

    private Set<Class<?>> findControllers(Object basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Object getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.getHandlerExecution(handlerKey);
    }
}
