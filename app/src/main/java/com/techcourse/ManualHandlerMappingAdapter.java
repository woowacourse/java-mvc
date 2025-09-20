package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;

public class ManualHandlerMappingAdapter implements HandlerMapping {

    private final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

    @Override
    public void initialize() {
        manualHandlerMapping.initialize();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return manualHandlerMapping.getHandler(request.getRequestURI());
    }
}
