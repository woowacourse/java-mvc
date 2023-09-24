package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

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
            .orElseThrow(() -> new IllegalArgumentException("매칭되는 핸들러가 없습니다."));
    }
}
