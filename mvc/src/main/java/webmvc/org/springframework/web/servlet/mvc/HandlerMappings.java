package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMapping = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        this.handlerMapping.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMapping.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Not found handler mapping"));
    }

    public void initialize() {
        handlerMapping.forEach(HandlerMapping::initialize);
    }
}
