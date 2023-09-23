package webmvc.org.springframework.web.servlet;

import static java.util.Collections.emptyList;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappings {

    private final List<HandlerMapping> items = new ArrayList<>();

    public HandlerMappings() {
        this(emptyList());
    }

    public HandlerMappings(final List<HandlerMapping> handlerMappings) {
        handlerMappings.forEach(HandlerMapping::initialize);
        items.addAll(handlerMappings);
    }

    public void add(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        items.add(handlerMapping);
    }

    public Optional<Object> getHandler(final HttpServletRequest httpServletRequest) {
        return items.stream()
                .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
