package webmvc.org.springframework.web.servlet.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappings {

    private final List<HandlerMapping> values;

    public HandlerMappings(final List<HandlerMapping> values) {
        this.values = values;
    }

    public void initialize() {
        for (final HandlerMapping handlerMapping : values) {
            handlerMapping.initialize();
        }
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        return values.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst();
    }

}
