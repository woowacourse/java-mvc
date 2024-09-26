package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;

public class ControllerHandlerMappingAdaptor implements HandlerMapping {

    private final ManualHandlerMapping handlerMapping;

    public ControllerHandlerMappingAdaptor(ManualHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public void initialize() {
        handlerMapping.initialize();
    }

    @Override
    public HandlerAdaptor getHandler(HttpServletRequest request) {
        Controller handler = handlerMapping.getHandler(request.getRequestURI());
        return new ControllerHandlerAdaptor(handler);
    }
}
