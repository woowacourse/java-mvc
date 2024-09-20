package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.NotFoundException;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
        initialize();
    }

    public void initialize() {
        Reflections reflections = new Reflections(ClasspathHelper.forJavaClassPath());
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.stream()
                .map(Class::getDeclaredMethods)
                .forEach(handlerExecutions::addHandlerExecution);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(requestMethod));
        if (handlerExecutions.containsHandlerKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        throw new NotFoundException("일치하는 handlerKey가 없습니다");
    }
}
