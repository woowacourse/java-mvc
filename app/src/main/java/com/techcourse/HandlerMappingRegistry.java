package com.techcourse;

import com.techcourse.exception.NotFoundHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                              .map(handlerMapping -> handlerMapping.getHandler(request))
                              .findFirst()
                              .orElseThrow(() -> new NotFoundHandlerMapping("해당 핸들러 매핑을 찾지 못했습니다."));
    }
}
