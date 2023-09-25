package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HandlerMappings {

    private final Set<HandlerMapping> handlerMappings;
    private boolean isInitialized = false;

    public HandlerMappings() {
        this.handlerMappings = new HashSet<>();
    }

    public void add(final HandlerMapping handlerMappings) {
        if (isInitialized) {
            throw new IllegalArgumentException("Already initialized");
        }
        this.handlerMappings.add(handlerMappings);
    }

    public void initialize() {
        if (isInitialized) {
            return;
        }
        handlerMappings.forEach(HandlerMapping::initialize);
        isInitialized = true;
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("요청에 맞는 Handler가 없습니다."));
    }
}
