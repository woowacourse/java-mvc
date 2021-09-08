package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandlerMapping(HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
