package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotFoundException;

public class HandlerMappers {

    private final List<HandlerMapper> handlerMappers = new ArrayList<>();

    public void init() {
        for (HandlerMapper handlerMapper : handlerMappers) {
            handlerMapper.initialize();
        }
    }

    public void addHandlerMapper(final HandlerMapper handlerMapper) {
        handlerMappers.add(handlerMapper);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappers.stream()
                .map(handlerMapper -> handlerMapper.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(HandlerNotFoundException::new);
    }
}
