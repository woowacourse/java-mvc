package com.techcourse.mvc;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class ManualHandlerMappingAdapter implements HandlerMapping {

    private final ManualHandlerMapping manualHandlerMapping;

    public ManualHandlerMappingAdapter() {
        manualHandlerMapping = new ManualHandlerMapping();
    }

    @Override
    public void initialize() {
        manualHandlerMapping.initialize();
    }

    @Override
    public Object getHandler(final HttpServletRequest httpServletRequest) {
        return manualHandlerMapping.getHandler(httpServletRequest.getRequestURI());
    }
}
