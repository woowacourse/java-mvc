package nextstep.mvc.registry;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.HandlerMapping;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void initializeHandlerMappings() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object findHandler(HttpServletRequest request) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findAny()
            .orElseThrow(() -> new IllegalStateException("요청에 해당하는 Handler를 찾을 수 없습니다."));
    }
}
