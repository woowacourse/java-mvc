package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
                .map(hmp -> hmp.getHandler(httpServletRequest))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("[ERROR] Handler 가 존재하지 않습니다."));
    }

    public void initHandlerMappings() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }
}
