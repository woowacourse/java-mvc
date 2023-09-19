package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappingRegistry {

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(HttpServletRequest httpServletRequest) {
        Optional<Object> availableHandler = handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findAny();
        if (availableHandler.isPresent()) {
            return Optional.of(availableHandler.get());
        }
        return Optional.empty();
    }
}
