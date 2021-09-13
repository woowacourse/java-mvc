package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappingRegistry {

    private static final List<HandlerMapping> handlerMappings = new ArrayList<>();

    private HandlerMappingRegistry() {
    }

    public static void initAll() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public static void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public static Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny();
    }
}
