package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final ControllerScanner controllerScanner;
    private final HandlerExecutionExtractor handlerExecutionExtractor;
    private final HandlerExecutionStorage handlerExecutionStorage;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this(new ControllerScanner(), basePackage);
    }

    public AnnotationHandlerMapping(final ControllerScanner controllerScanner, final Object... basePackage) {
        this.basePackage = basePackage;
        this.controllerScanner = controllerScanner;
        this.handlerExecutionExtractor = new HandlerExecutionExtractor();
        this.handlerExecutionStorage = new HandlerExecutionStorage();
    }

    @Override
    public void initialize() {
        final Map<Class<?>, Object> controllers = controllerScanner.scan(basePackage);
        handlerExecutionStorage.addAll(handlerExecutionExtractor.extract(controllers));

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutionStorage.get(new HandlerKey(request));
    }
}
