package com.techcourse.handlermapping;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.techcourse.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombinedHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(CombinedHandlerMapping.class);

    private final ManualHandlerMapping manualHandlerMapping;
    private final AnnotationHandlerMapping annotationHandlerMapping;

    public CombinedHandlerMapping(String basePackage) {
        annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        manualHandlerMapping = new ManualHandlerMapping();
    }

    @Override
    public void initialize() {
        annotationHandlerMapping.initialize();
        manualHandlerMapping.initialize();
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        Controller controller = (Controller) annotationHandlerMapping.getHandler(request);
        if (controller != null) {
            log.info("annotationHandlerMapping.getHandler : {}", controller);
            return controller;
        }
        controller = manualHandlerMapping.getHandler(request);
        log.info("manualHandlerMapping.getHandler : {}", controller);
        return controller;
    }
}
