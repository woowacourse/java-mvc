package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerMapping handlerMapping1 = handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.supports(request))
                .findAny()
                .orElseThrow();
        return handlerMapping1.getHandler(request);
    }
}
