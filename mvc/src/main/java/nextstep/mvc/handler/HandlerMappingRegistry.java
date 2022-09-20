package nextstep.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nextstep.mvc.HandlerMapping;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        this.handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No matched handler for request : " + request.getRequestURI()));
    }
}
