package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.exception.HandlerNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> mappings;

    public HandlerMappings() {
        this(new ArrayList<>());
    }

    public HandlerMappings(List<HandlerMapping> mappings) {
        this.mappings = mappings;
    }

    public void initialize() {
        mappings.forEach(HandlerMapping::initialize);
    }

    public void add(HandlerMapping handlerMapping) {
        mappings.add(handlerMapping);
    }

    public Object findHandler(HttpServletRequest request) {
        return mappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException(request.getRequestURI()));
    }
}
