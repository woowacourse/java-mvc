package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        handlerMappings = new ArrayList<>();
    }

    public void initialize(final String packageName) {
        handlerMappings.add(new AnnotationHandlerMapping(packageName));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Optional<Object> getHandler(final Request request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.support(request))
                .findFirst()
                .map(handlerMapping -> handlerMapping.getHandler(request));
    }
}
