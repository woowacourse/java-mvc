package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nextstep.mvc.HandlerMapping;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry () {
        this.handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        Optional<Object> handler = Optional.empty();
        for (HandlerMapping handlerMapping : handlerMappings) {
            handler = Optional.ofNullable(handlerMapping.getHandler(request));
        }
        return handler;
    }
}
