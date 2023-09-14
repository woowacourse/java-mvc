package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class ManualHandlerMappingAdapter implements HandlerMapping {

    private final ManualHandlerMapping mappings = new ManualHandlerMapping();

    @Override
    public void initialize() {
        mappings.initialize();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return mappings.getHandler(request.getRequestURI());
    }
}
