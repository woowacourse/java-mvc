package webmvc.org.springframework.web.servlet.mvc.handler_mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class HandlerMappingRegistry {

    private final Set<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(Set<HandlerMapping> handlerMappings) {
        this.handlerMappings = new HashSet<>(handlerMappings);
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(it -> it.getHandler(request))
                .filter(Optional::isPresent)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("요청 URI을 처리할 핸들러가 없습니다. 요청 URI=" + request.getRequestURI()));
    }
}
