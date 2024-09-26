package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping)  {
        handlerMappings.add(handlerMapping);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        if (handlerMappings.isEmpty()) {
            return null;
        }
        Object handler = null;
        for (HandlerMapping handlerMapping : handlerMappings) {
            handler = handlerMapping.getHandler(request);
            if (handler != null) break;
        }
        if (handler instanceof Controller) {
            return new ControllerHandlerAdapter((Controller) handler);
        }

        return (HandlerExecution) handler;
    }
}
