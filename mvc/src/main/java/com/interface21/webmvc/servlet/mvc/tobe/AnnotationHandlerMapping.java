package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner();
    }

    @Override
    public void initialize() {
        String[] packages = new String[basePackage.length];
        for (int i = 0; i < basePackage.length; i++) {
            packages[i] = basePackage[i].toString();
        }

        Map<HandlerKey, HandlerExecution> scannedHandlers = controllerScanner.scan(packages);
        handlerExecutions.putAll(scannedHandlers);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);

        HandlerKey key = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(key);
    }

}
