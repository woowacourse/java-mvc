package webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappings {
    private final List<HandlerMapping> mappings;

    public HandlerMappings() {
        this.mappings = new ArrayList<>();
    }

    public void init(final HandlerMapping... mappings) {
        this.mappings.add(new AnnotationHandlerMapping());
        this.mappings.addAll(List.of(mappings));
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : mappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (Objects.nonNull(handler)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }
}
