package com.interface21.webmvc.servlet.mvc;

import com.interface21.NotFoundException;
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
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                return handlerMapping.getHandler(request);
            } catch (NotFoundException e) {
                System.out.println("해당하는 mappings아님");
            }
        }
        return handlerMappings;
    }
}
