package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class WrappedManualHandlerMapping implements HandlerMapping {

    private final ManualHandlerMapping handlerMapping = new ManualHandlerMapping();

    @Override
    public void initialize() {
        handlerMapping.initialize();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerMapping.getHandler(request.getRequestURI());
    }
}
