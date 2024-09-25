package com.techcourse.framework.ManualHandler;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingAdapter;

public class ManualHandlerMappingAdapter implements HandlerMappingAdapter {

    private final ManualHandlerMapping manualHandlerMapping;

    public ManualHandlerMappingAdapter(ManualHandlerMapping manualHandlerMapping) {
        this.manualHandlerMapping = manualHandlerMapping;
    }

    @Override
    public void initialize() {
        manualHandlerMapping.initialize();
    }

    @Override
    //todo Optional????
    public HandlerAdapter getHandler(HttpServletRequest request) {
        Controller handler = manualHandlerMapping.getHandler(request.getRequestURI());
        if (handler == null) {
            return null;
        }
        return new ManualHandlerAdapter(handler);
    }
}
