package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappings {
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(final List<HandlerMapping> handlerMappings) {
        handlerMappings.forEach(HandlerMapping::initialize);
        this.handlerMappings = handlerMappings;
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("can't find suitable handler" + request.getRequestURI()));
    }
}
