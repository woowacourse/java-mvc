package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하는 handler가 존재하지 않습니다."));
    }
}
