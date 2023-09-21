package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> mappings = new ArrayList<>();

    public void add(final HandlerMapping handlerMapping) {
        mappings.add(handlerMapping);
    }

    public void init() {
        mappings.forEach(HandlerMapping::initialize);
    }

    public Object findHandler(final HttpServletRequest request) {
        return mappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Handler Not Found"));
    }
}
