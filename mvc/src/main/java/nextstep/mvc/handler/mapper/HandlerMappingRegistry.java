package nextstep.mvc.handler.mapper;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapper> handlerMappers;

    public HandlerMappingRegistry() {
        this.handlerMappers = new ArrayList<>();
    }

    public void addHandlerMapping(final HandlerMapper handlerMapper) {
        handlerMappers.add(handlerMapper);
    }

    public void init() {
        handlerMappers.forEach(HandlerMapper::initialize);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappers.stream()
                .map(handlerMapper -> handlerMapper.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow();
    }
}
