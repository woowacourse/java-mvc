package nextstep.mvc;

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

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }


    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 Handler 요청입니다."));
    }
}
