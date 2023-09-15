package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotFoundException;

public class HandlerMapper {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void init() throws Exception {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(HandlerNotFoundException::new);
    }
}
