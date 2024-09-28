package com.techcourse.mapping;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.handlerExist(request))
                .findFirst()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .orElseThrow(() -> new NoSuchElementException("처리할 수 있는 핸들러를 찾지 못했습니다."));
    }
}
