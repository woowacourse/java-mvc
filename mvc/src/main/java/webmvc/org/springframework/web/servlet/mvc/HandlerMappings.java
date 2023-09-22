package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerMappings addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
        handlerMapping.initialize();
        return this;
    }

    public Object getHandler(HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("There is not matched handler"));
    }
}
