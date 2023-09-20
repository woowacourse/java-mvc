package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMethod;

public class ManualHandlerMappingAdapter implements HandlerMapping {

    private final ManualHandlerMapping manualHandlerMapping;

    public ManualHandlerMappingAdapter() {
        this.manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) throws Exception {
        Controller controller = manualHandlerMapping.getHandler(request.getRequestURI());
        if (controller == null) {
            return null;
        }
        return new HandlerExecution(new HandlerMethod(controller, controller.getClass()
                .getDeclaredMethod("execute", HttpServletRequest.class, HttpServletResponse.class)));
    }
}
