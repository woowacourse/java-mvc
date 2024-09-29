package com.interface21.webmvc.servlet.mvc.tobe.fixture;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;

import samples.TestController;

public class FakeManualHandlerMapping implements HandlerMapping {
    private final Map<String, Object> handlers;

    public FakeManualHandlerMapping() {
        this.handlers = new HashMap<>();
    }

    public void initialize() {
        handlers.put("/get-test", new TestController());
        handlers.put("/post-test", new TestController());
        handlers.put("/no-request-method-test", new TestController());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();

        return handlers.get(requestURI);
    }
}
