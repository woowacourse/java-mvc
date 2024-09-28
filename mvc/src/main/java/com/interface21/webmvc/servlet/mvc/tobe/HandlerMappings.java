package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping)  {
        handlerMappings.add(handlerMapping);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        if (handlerMappings.isEmpty()) {
            return null;
        }

        HandlerExecution handler = handlerMappings.stream()
                .map(handlerMapping -> getHandler(request))
                .filter(Objects::isNull)
                .findFirst()
                .orElse(null);

        if (handler instanceof Controller) {
            return new ControllerHandlerAdapter((Controller) handler);
        }

        return handler;
    }
}
