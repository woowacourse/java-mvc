package web.org.springframework.web.support.handler;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappers {

    private final List<HandlerMapper> handlerMappings = new ArrayList<>();

    public void initialize() {
        handlerMappings.forEach(HandlerMapper::initialize);
    }

    public void addHandlerMapper(HandlerMapper handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Handler Not Found"));
    }

}
