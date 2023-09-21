package com.techcourse.support.web.mapping;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class ManualHandlerMappingWrapper implements HandlerMapping {

    private final ManualHandlerMapping mapping = new ManualHandlerMapping();

    @Override
    public void initialize() {
        mapping.initialize();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return mapping.getHandler(request.getRequestURI());
    }
}
