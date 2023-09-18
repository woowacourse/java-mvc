package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(HandlerMapping... handlerMappings) {
        this.handlerMappings = Arrays.stream(handlerMappings).collect(Collectors.toList());
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                              .map(handler -> handler.getHandler(request))
                              .filter(Objects::nonNull)
                              .findFirst()
                              .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 URL입니다."));
    }
}
