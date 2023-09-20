package com.techcourse.support.web.handler.adaptor;

import com.techcourse.ManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class ManualHandlerMappingWrapped implements HandlerMapping {

    private final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

    @Override
    public void initialize() {
        manualHandlerMapping.initialize();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        return manualHandlerMapping.getHandler(requestURI);
    }
}
