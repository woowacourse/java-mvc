package com.techcourse.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class HandlerMappingAdapter implements HandlerMapping {

    private final ManualHandlerMapping manualHandlerMapping;

    public HandlerMappingAdapter(ManualHandlerMapping manualHandlerMapping) {
        this.manualHandlerMapping = manualHandlerMapping;
    }

    @Override
    public void initialize() {
        manualHandlerMapping.initialize();
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        Controller controller = manualHandlerMapping.getHandler(request.getRequestURI());
        if (Objects.isNull(controller)) {
            return null;
        }
        return new HandlerExecutionAdapter(controller);
    }
}
