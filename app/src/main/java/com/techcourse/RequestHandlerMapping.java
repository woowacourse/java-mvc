package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;

public class RequestHandlerMapping {

    private final ManualHandlerMapping manualHandlerMapping;
    private final AnnotationHandlerMapping annotationHandlerMapping;

    public RequestHandlerMapping() {
        this.manualHandlerMapping = new ManualHandlerMapping();
        this.annotationHandlerMapping = new AnnotationHandlerMapping("com");
        initHandlerMapping();
    }

    private void initHandlerMapping() {
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();
    }

    public Object getHandler(final HttpServletRequest request) {
        final Controller handlerFromManualHandlerMapping = getHandlerFromManualHandlerMapping(request);
        if (handlerFromManualHandlerMapping != null) {
            return handlerFromManualHandlerMapping;
        }

        return getHandlerFromAnnotationHandlerMapping(request);
    }

    private Controller getHandlerFromManualHandlerMapping(final HttpServletRequest request) {
        return manualHandlerMapping.getHandler(request.getRequestURI());
    }

    private HandlerExecution getHandlerFromAnnotationHandlerMapping(final HttpServletRequest request) {
        return annotationHandlerMapping.findHandler(request).orElse(null);
    }
}
