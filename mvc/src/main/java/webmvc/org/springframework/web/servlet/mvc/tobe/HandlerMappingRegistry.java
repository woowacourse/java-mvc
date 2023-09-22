package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("요청과 매핑되는 핸들러가 존재하지 않습니다."));
    }
}
