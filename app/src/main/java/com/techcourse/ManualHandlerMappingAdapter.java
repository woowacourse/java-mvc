package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class ManualHandlerMappingAdapter implements HandlerMapping {

    private final ManualHandlerMapping manualHandlerMapping;

    public ManualHandlerMappingAdapter(final ManualHandlerMapping manualHandlerMapping) {
        this.manualHandlerMapping = manualHandlerMapping;
    }

    @Override
    public void initialize() {
        manualHandlerMapping.initialize();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return manualHandlerMapping.getHandler(request.getRequestURI());
    }
}
