package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HandlerMappings {

    Set<HandlerMapping> values = new HashSet<>();

    public void init() {
        values.forEach(HandlerMapping::initialize);
    }

    public void add(final HandlerMapping handlerMapping) {
        values.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        return values.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow();
    }
}
