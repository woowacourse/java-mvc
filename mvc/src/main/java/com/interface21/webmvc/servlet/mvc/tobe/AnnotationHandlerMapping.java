package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final ControllerScanner controllerScanner;
    private Map<HandlerKey, HandlerExecution> handlerExecutions;


    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.controllerScanner = new ControllerScanner();
        initialize();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        controllerScanner.instantiateControllers(controllerClasses);
        this.handlerExecutions = controllerScanner.getHandlerExecutions();
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        String requestURI = request.getRequestURI();

        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
