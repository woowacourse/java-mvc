package com.techcourse;

import com.interface21.webmvc.servlet.Handler;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public boolean hasHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .anyMatch(handlerMapping -> hasHandler(request));
    }

    public Handler getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> hasHandler(request))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(request + "에 해당하는 핸들러를 찾을 수 없습니다"))
                .getHandler(request);
    }
}
