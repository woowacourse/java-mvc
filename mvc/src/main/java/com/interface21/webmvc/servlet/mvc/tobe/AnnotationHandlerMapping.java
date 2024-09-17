package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<Controller> CONTROLLER_ANNOTATION = Controller.class;

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;
    private final InstancePool instancePool;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
        this.instancePool = new InstancePool();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        List<Object> controllers = reflections.getTypesAnnotatedWith(CONTROLLER_ANNOTATION).stream()
                .map(instancePool::getInstance)
                .toList();
        controllers.forEach(handlerExecutions::setHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }


    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.findByUrlAndMethod(request.getRequestURI(), RequestMethod.from(request.getMethod()));
    }
}
