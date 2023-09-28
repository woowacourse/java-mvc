package webmvc.org.springframework.web.servlet.mvc.tobe.adapter;

import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotFoundException;

public class HandlerAdapterFinder {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterFinder(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter find(Object handler) {
        return handlerAdapters.stream()
            .filter(it -> it.supports(handler))
            .findFirst()
            .orElseThrow(HandlerNotFoundException::new);
    }
}
