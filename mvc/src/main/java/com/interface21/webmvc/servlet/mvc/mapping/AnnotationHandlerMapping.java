package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerExecutions;
import com.interface21.webmvc.servlet.mvc.InstancePool;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements RequestHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<Controller> CONTROLLER_ANNOTATION = Controller.class;

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping() {
        this(new Object[0]);
    }

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        List<Object> controllers = reflections.getTypesAnnotatedWith(CONTROLLER_ANNOTATION).stream()
                .map(clazz -> InstancePool.getSingleton().getInstance(clazz))
                .toList();
        controllers.forEach(handlerExecutions::setHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.findByUrlAndMethod(request.getRequestURI(), RequestMethod.from(request.getMethod()));
    }
}
