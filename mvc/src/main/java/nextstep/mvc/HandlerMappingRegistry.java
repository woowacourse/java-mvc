package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.exception.NoSuchRequestMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        this.handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public HandlerMapping getHandlerMapping(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> Objects.nonNull(handlerMapping.getHandler(request)))
                .findFirst().orElseThrow(() -> new NoSuchRequestMappingException("No such request mapping"));
    }
}
