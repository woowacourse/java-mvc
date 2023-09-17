package com.techcourse.mvc;

import com.techcourse.mvc.exception.UncheckedServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

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
    public HandlerExecution getHandler(final HttpServletRequest httpServletRequest) {
        final var handler = manualHandlerMapping.getHandler(httpServletRequest.getRequestURI());
        if (handler == null) {
            return null;
        }
        try {
            return new HandlerExecution(handler, handler.getClass().getMethod("execute", HttpServletRequest.class, HttpServletResponse.class));
        } catch (NoSuchMethodException e) {
            throw new UncheckedServletException(e);
        }
    }
}
