package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerMappingException;

public class HandlerMappings {

    private final List<HandlerMapping> mappings;

    public HandlerMappings(final Object... basePackages) {
        this.mappings = List.of(
            new AnnotationHandlerMapping(basePackages)
        );
    }

    public void initialize() {
        for (final HandlerMapping mapping : mappings) {
            mapping.initialize();
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return mappings.stream()
            .map(mapping -> mapping.getHandler(request))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(HandlerMappingException.NotFoundException::new);
    }
}
