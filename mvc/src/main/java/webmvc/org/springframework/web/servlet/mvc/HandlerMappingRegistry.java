package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import javassist.NotFoundException;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class HandlerMappingRegistry {

    private final Set<HandlerMapping> handlerMappings = new LinkedHashSet<>();

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object matchHandler(HttpServletRequest request) throws NotFoundException {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("cannot find handler for request"));
    }

}
