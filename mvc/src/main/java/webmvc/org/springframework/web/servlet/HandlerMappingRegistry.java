package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;


    public HandlerMappingRegistry(HandlerMapping... handlerMappings) {
        this.handlerMappings = Arrays.stream(handlerMappings)
                .collect(Collectors.toList());
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMappings(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny();
    }
}
