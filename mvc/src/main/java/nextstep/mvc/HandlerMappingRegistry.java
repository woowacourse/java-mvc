package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.exception.NoHandlerFoundException;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(NoHandlerFoundException::new);
    }
}
