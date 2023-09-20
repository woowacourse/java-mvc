package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.Objects;

public class ManualHandlerMappingAdapter implements HandlerMapping {

    private final ManualHandlerMapping manualHandlerMapping;

    public ManualHandlerMappingAdapter(ManualHandlerMapping manualHandlerMapping) {
        this.manualHandlerMapping = manualHandlerMapping;
    }

    @Override
    public void initialize() {
        manualHandlerMapping.initialize();
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return new ControllerHandlerExecution(manualHandlerMapping.getHandler(request.getRequestURI()));
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        return Objects.nonNull(manualHandlerMapping.getHandler(request.getRequestURI()));
    }
}
