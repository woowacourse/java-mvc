package com.interface21.webmvc.servlet.mvc;

import com.interface21.NotFoundException;
import com.interface21.SingletonManager;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        SingletonManager instance = SingletonManager.getInstance();
        instance.registerHandler(HandlerMapping.class);
        List<HandlerMapping> subTypesOf = instance.getSubTypesOf(HandlerMapping.class);
        subTypesOf.forEach(HandlerMapping::initialize);
        handlerMappings.addAll(subTypesOf);
    }

    public Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                return handlerMapping.getHandler(request);
            } catch (NotFoundException e) {}
        }
        throw new NotFoundException("일치하는 handler가 없습니다");
    }
}
